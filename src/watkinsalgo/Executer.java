package watkinsalgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import watkinsalgo.Geometry.*;
import watkinsalgo.util.DoublePair;

/**
 *
 * @author Евгений
 */
public class Executer {

    
    private final Canvas canvas;
    private GraphicsContext context;
    
    private ArrayList<Surface> surfaces = new ArrayList<>();
    private SurfaceEventsList listSurfacesEvents = new SurfaceEventsList();
    private PointsEventsList listPointsEvent = new PointsEventsList();
    
    public Executer(Canvas canvas) {
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
    }
    private ArrayList<Surface> drawnSurfaces = new ArrayList<>();
    private ArrayList<Segment> drawnSegments = new ArrayList<>();
    private ArrayList<Point> pointsList = new ArrayList<>();
    public void drawPicture() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        DoublePair border = findMinAndMaxY();
         
        for (int currentY = (int)border.getMaxValue(), endY = (int)border.getMinValue(); currentY >= endY; currentY--) {
            //добавление рассматриваемых плоскостей
            changeDrawnSurfacesList(currentY, true);
            //поиск всех точек, пересекающих эту плоскость, и составление из них отрезков
            //поиск точек пересечения между этими отрезками
            findAllIntersections(currentY);
            //сортировка
            pointsList =(ArrayList<Point>) pointsList.stream().sorted((a, b) -> Double.compare(a.getX(), b.getX())).collect(Collectors.toList());
            //отрисовка
            Point last = new Point(0, currentY, 0);
            for (Point p : pointsList) {
                if (drawnSegments.size() > 0) {
                    double maxZ = Double.MIN_VALUE;
                    Segment chosen = null;
                    for (Segment seg : drawnSegments) {
                        double z = seg.getZOnX(p.getX());
                        if (z > maxZ) {
                            maxZ = z;
                            chosen = seg;
                        }
                    }
                    context.setStroke(chosen.getSurfaceColor());
                    double centerX = canvas.getWidth() / 2,
                           y = canvas.getHeight() / 2 + currentY;
                    context.strokeLine(centerX + last.getX(), y, centerX + p.getX(), y);
                }
                //актуализация отрезков
                ArrayList<PointEvent> events = listPointsEvent.getEvent(p.getX());
                events.stream().map((event) -> {
                    if (event.event == PointEventsList.START) {
                        drawnSegments.add(event.point.getParent());
                    }
                    return event;
                }).filter((event) -> (event.event == PointEventsList.FINISH)).forEach((event) -> {
                    drawnSegments.remove(event.point.getParent());
                });
                last = p;
            }
            //удаление уже рассмотренных плоскостей
            changeDrawnSurfacesList(currentY, false);
        }
    }
    
    /**
     * Поиск всех точек пересечения поверхностей с плоскостью У, а также поиск пересечений между отрезками получающимися.
     * @param currentY 
     */
    private void findAllIntersections(double currentY) {
        drawnSegments.clear();
        pointsList.clear();
        listPointsEvent.map.clear();
        for (Surface surface : drawnSurfaces) {
            Segment segment = surface.getIntersectionWithY(currentY);
            drawnSegments.add(segment);
            pointsList.add(segment.getStart());
            pointsList.add(segment.getFinish());
            listPointsEvent.addEvent(new PointEvent(PointEventsList.START, segment.getStart()), segment.getStart().getX());
            listPointsEvent.addEvent(new PointEvent(PointEventsList.FINISH, segment.getFinish()), segment.getFinish().getX());
        }
        for (int i = 0, number = drawnSegments.size(); i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                Point intersection = drawnSegments.get(i).getIntersectionWithSegment(drawnSegments.get(j));
                if (intersection != null) {
                    pointsList.add(intersection);
                    listPointsEvent.addEvent(new PointEvent(PointEventsList.INTERSECTION, intersection), intersection.getX());
                }
            }
        }
    }
    
    private void changeDrawnSurfacesList(double currentY, boolean isAdd) {
        ArrayList<SurfaceEvent> events = listSurfacesEvents.getEvent(currentY);
        if (events == null) return;
        events.stream().forEach(event ->
        {
            if (isAdd && event.isStart) {
                drawnSurfaces.add(event.surface);
            } else {
                if (!isAdd && !event.isStart) {
                    drawnSurfaces.remove(event.surface);
                }
            }
        });
    }
    
    private DoublePair findMinAndMaxY() {
        DoublePair border = new DoublePair(Double.MAX_VALUE, Double.MIN_VALUE);
        surfaces.stream().forEach(
            current -> {
                DoublePair pair = current.getMinimumAndMaximumY();
                listSurfacesEvents.addEvent(new SurfaceEvent(current, true), pair.getMaxValue());
                listSurfacesEvents.addEvent(new SurfaceEvent(current, false), pair.getMinValue());
                border.changeValue(pair);
            }
        );
        return border;
    }
    
    enum PointEventsList {
        START,  FINISH, INTERSECTION
    };
    
    class PointEvent {
        PointEventsList event;
        Point point;

        public PointEvent(PointEventsList event, Point point) {
            this.event = event;
            this.point = point;
        }
    }
    
    class PointsEventsList {
        HashMap<Double, ArrayList<PointEvent>> map = new HashMap<>();
        
        void addEvent(PointEvent event, double x) {
            if (map.get(x) == null) {
                map.put(x, new ArrayList<>());
            }
            map.get(x).add(event);
        }
        
        ArrayList<PointEvent> getEvent(Double x) {
            return map.get(x);
        }
    }
    
    class SurfaceEvent {
        Surface surface;
        boolean isStart;

        public SurfaceEvent(Surface surface, boolean isStart) {
            this.surface = surface;
            this.isStart = isStart;
        }
    }
    
    class SurfaceEventsList {
        HashMap<Double, ArrayList<SurfaceEvent>> list = new HashMap<>();
        
        void addEvent(SurfaceEvent event, double y) {
            if (list.get(y) == null) {
                ArrayList<SurfaceEvent> events = new ArrayList<>();
                list.put(y, events);
            } 
            list.get(y).add(event);
        } 
        
        ArrayList<SurfaceEvent> getEvent(Double y) {
            return list.get(y);
        }
    }
}
