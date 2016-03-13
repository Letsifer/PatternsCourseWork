package watkinsalgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import watkinsalgo.Geometry.*;
import watkinsalgo.util.DoublePair;

/**
 *
 * @author Евгений
 */
public class Executer {

//    public AnimationTimer animationTimer = new AnimationTimer() {
//        @Override
//        public void handle(long now) {
//            context.strokeLine(x1, y, x2, y);
//        }
//    };
    ArrayList<Segment> result = new ArrayList<>();
    int indexResult = 0;
    Segment segmentResult;

    public void initTimer(int currentY, int index) {

        for (; index < setBorders.size() && setBorders.get(index) >= currentY; index++) {
            changeDrawnSurfacesList(currentY, true);
        }
        findAllIntersections(currentY);
        pointsList = (ArrayList<Point>) pointsList.stream().sorted((a, b) -> Double.compare(a.getX(), b.getX())).collect(Collectors.toList());
        Point last = new Point(0, currentY, 0);
        for (Point p : pointsList) {
            ArrayList<PointEvent> events = listPointsEvent.getEvent((int) p.getX());
            for (PointEvent event : events) {
                if (event.event == PointEventsList.START) {
                    drawnSegments.add(event.point.getParent());
                }
            }
            if (drawnSegments.size() > 0) {
                double maxZ = -Double.MAX_VALUE;
                Segment chosen = null;
                for (Segment seg : drawnSegments) {
                    double z = seg.getZOnX(p.getX());
                    if (z > maxZ) {
                        maxZ = z;
                        chosen = seg;
                    }
                }
                if (chosen != null && maxZ > -Double.MAX_VALUE) {
//                    context.setStroke(chosen.getSurfaceColor());
                    double x1 = canvas.getWidth() / 4 + last.getX() * 2,
                            x2 = canvas.getWidth() / 4 + p.getX() * 2,
                            y = canvas.getHeight() / 2 + currentY;
                    Segment segment = new Segment(new Point(x1, y, 0), new Point(x2, y, 0), chosen.getSurfaceColor());
                    result.add(segment);
//                    context.strokeLine(x1, y, x2, y);
                }

            }
            //актуализация отрезков
            for (PointEvent event : events) {
                if (event.event == PointEventsList.FINISH) {
                    drawnSegments.remove(event.point.getParent());
                }
            }
            last = p;
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (indexResult == result.size() - 1) {
                    stop();
                    return;
                }
                segmentResult = result.get(indexResult++);
                context.setStroke(segmentResult.getSurfaceColor());
                context.strokeLine(segmentResult.getStart().getX(), segmentResult.getStart().getY(), segmentResult.getFinish().getX(), segmentResult.getFinish().getY());
            }
        };
        timer.start();
    }

    private final Canvas canvas;
    private GraphicsContext context;

    private Set<Double> borders = new HashSet<Double>();
    private ArrayList<Surface> surfaces = new ArrayList<>();
    private SurfaceEventsList listSurfacesEvents = new SurfaceEventsList();
    private PointsEventsList listPointsEvent = new PointsEventsList();

    public Executer(Canvas canvas) {
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();

//          Point[] points = new Point[]{
//            new Point(0, 0, 0),
//            new Point(50, 50, 0),
//            new Point(100, 0, 0)
//        };
//        addSurface(Color.RED, points);
//
//        points = new Point[]{
//            new Point(0, 40, -70),
//            new Point(10, 50, -50),
//            new Point(65, -5, 100),
//            new Point(55, -10, 100)
//        };
//        addSurface(Color.BLUE, points);
//        
//        points = new Point[]{
//            new Point(25, 0, -70),
//            new Point(50, 0, -50),
//            new Point(50, 70, 100),
//            new Point(25, 50, 100)
//        };
//        addSurface(Color.YELLOW, points);
        Point[] points = new Point[]{
            new Point(10, 30, 0),
            new Point(10, 70, 0),
            new Point(50, 100, 0),
            new Point(100, 70, 0)
        };
        addSurface(Color.RED, points);

        points = new Point[]{
            new Point(-70, -70, 0),
            new Point(-100, 30, 0),
            new Point(-30, 100, 0),
            new Point(50, 50, 0),
            new Point(70, -70, 0)
        };
        addSurface(Color.BLUE, points);

        points = new Point[]{
            new Point(10, -60, 0),
            new Point(-20, 20, 0),
            new Point(40, 20, 0)
        };
        addSurface(Color.GREEN, points);

        points = new Point[]{
            new Point(10, 30, 0),
            new Point(10, 70, 0),
            new Point(50, 100, 0),
            new Point(100, 70, 0)
        };
        addSurface(Color.BROWN, points);

        points = new Point[]{
            new Point(10, -60, 0),
            new Point(-20, 20, 0),
            new Point(40, 20, 0)
        };
        addSurface(Color.BLACK, points);

        Random random = new Random();
        for (Surface s : surfaces) {
            s.XRotate(random.nextInt(60));
            s.YRotate(1 + random.nextInt(170));
            s.ZRotate(random.nextInt(180));
        }
    }

    private void addSurface(Color color, Point[] points) {
        Surface added = new Surface(color);
        for (int i = 0, n = points.length; i < n; i++) {
            added.addEdge(points[i % n], points[(i + 1) % n]);
        }
        surfaces.add(added);
    }

    private ArrayList<Surface> drawnSurfaces = new ArrayList<>();
    private ArrayList<Segment> drawnSegments = new ArrayList<>(),
            segments = new ArrayList<>();
    private ArrayList<Point> pointsList = new ArrayList<>();
    ArrayList<Double> setBorders;

    public void drawPicture(boolean isOnce) {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        DoublePair border = findMinAndMaxY();
        setBorders = (ArrayList<Double>) borders.stream().sorted((a, b) -> Double.compare(b, a)).collect(Collectors.toList());
        for (int currentY = (int) border.getMaxValue() + 1, endY = (int) border.getMinValue(), index = 0; currentY >= endY; currentY--) {
            if (isOnce) {
                drawLine(currentY, index);
            } else {
                initTimer(currentY, index);
            }

        }
    }

//    int x1, x2, y;
    private void drawLine(int currentY, int index) {
        //добавление рассматриваемых плоскостей
        for (; index < setBorders.size() && setBorders.get(index) >= currentY; index++) {
            changeDrawnSurfacesList(currentY, true);
        }
        //поиск всех точек, пересекающих эту плоскость, и составление из них отрезков
        //поиск точек пересечения между этими отрезками
        findAllIntersections(currentY);
        //сортировка
        pointsList = (ArrayList<Point>) pointsList.stream().sorted((a, b) -> Double.compare(a.getX(), b.getX())).collect(Collectors.toList());
        //отрисовка
        Point last = new Point(0, currentY, 0);
        for (Point p : pointsList) {
            ArrayList<PointEvent> events = listPointsEvent.getEvent((int) p.getX());
            for (PointEvent event : events) {
                if (event.event == PointEventsList.START) {
                    drawnSegments.add(event.point.getParent());
                }
            }
            if (drawnSegments.size() > 0) {
                double maxZ = -Double.MAX_VALUE;
                Segment chosen = null;
                for (Segment seg : drawnSegments) {
                    double z = seg.getZOnX(p.getX());
                    if (z > maxZ) {
                        maxZ = z;
                        chosen = seg;
                    }
                }
                if (chosen != null && maxZ > -Double.MAX_VALUE) {
                    context.setStroke(chosen.getSurfaceColor());
                    double x1 = canvas.getWidth() / 4 + last.getX() * 2,
                            x2 = canvas.getWidth() / 4 + p.getX() * 2,
                            y = canvas.getHeight() / 2 + currentY;
                    context.strokeLine(x1, y, x2, y);
                }

            }
            //актуализация отрезков
            for (PointEvent event : events) {
                if (event.event == PointEventsList.FINISH) {
                    drawnSegments.remove(event.point.getParent());
                }
            }
            last = p;
        }
    }

    /**
     * Поиск всех точек пересечения поверхностей с плоскостью У, а также поиск
     * пересечений между отрезками получающимися.
     *
     * @param currentY
     */
    private void findAllIntersections(double currentY) {
        drawnSegments.clear();
        pointsList.clear();
        listPointsEvent.map.clear();
        for (Surface surface : drawnSurfaces) {
            Segment segment = surface.getIntersectionWithY(currentY);
            if (segment == null) {
                continue;
            }
            if (segment.getFinish().getX() < segment.getStart().getX()) {
                segment = new Segment(segment.getFinish(), segment.getStart(), surface.getSurfaceColor());
            }
            segments.add(segment);
            pointsList.add(segment.getStart());
            pointsList.add(segment.getFinish());
            listPointsEvent.addEvent(new PointEvent(PointEventsList.START, segment.getStart()), (int) segment.getStart().getX());
            listPointsEvent.addEvent(new PointEvent(PointEventsList.FINISH, segment.getFinish()), (int) segment.getFinish().getX());
        }
        for (int i = 0, number = drawnSegments.size(); i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                Point intersection = drawnSegments.get(i).getIntersectionWithSegment(drawnSegments.get(j));
                if (intersection != null) {
                    pointsList.add(intersection);
                    listPointsEvent.addEvent(new PointEvent(PointEventsList.INTERSECTION, intersection), (int) intersection.getX());
                }
            }
        }
    }

    private void changeDrawnSurfacesList(int currentY, boolean isAdd) {
        ArrayList<SurfaceEvent> events = listSurfacesEvents.getEvent(currentY);
        if (events == null) {
            return;
        }
        events.stream().forEach(event
                -> {
            if (isAdd && event.isStart) {
                drawnSurfaces.add(event.surface);
            } else if (!isAdd && !event.isStart) {
                drawnSurfaces.remove(event.surface);
            }
        });
    }

    private DoublePair findMinAndMaxY() {
        DoublePair border = new DoublePair(Double.MAX_VALUE, -Double.MAX_VALUE);
        surfaces.stream().forEach(
                current -> {
                    DoublePair pair = current.getMinimumAndMaximumY();
                    listSurfacesEvents.addEvent(new SurfaceEvent(current, true), (int) pair.getMaxValue());
                    listSurfacesEvents.addEvent(new SurfaceEvent(current, false), (int) pair.getMinValue());
                    borders.add(pair.getMaxValue());
                    borders.add(pair.getMinValue());
                    border.changeValue(pair);
                }
        );
        return border;
    }

    enum PointEventsList {
        START, FINISH, INTERSECTION
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

        HashMap<Integer, ArrayList<PointEvent>> map = new HashMap<>();

        void addEvent(PointEvent event, int x) {
            if (map.get(x) == null) {
                map.put(x, new ArrayList<>());
            }
            map.get(x).add(event);
        }

        ArrayList<PointEvent> getEvent(Integer x) {
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

        HashMap<Integer, ArrayList<SurfaceEvent>> list = new HashMap<>();

        void addEvent(SurfaceEvent event, int y) {
            if (list.get(y) == null) {
                ArrayList<SurfaceEvent> events = new ArrayList<>();
                list.put(y, events);
            }
            list.get(y).add(event);
        }

        ArrayList<SurfaceEvent> getEvent(Integer y) {
            return list.get(y);
        }
    }
}
