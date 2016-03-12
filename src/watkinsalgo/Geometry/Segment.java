package watkinsalgo.Geometry;

import java.util.Objects;
import watkinsalgo.util.DoublePair;

import static java.lang.Math.*;
import javafx.scene.paint.Color;

/**
 * Класс ребра плоскости.
 * @author Евгений
 */
public class Segment {
    private final static double EPS = 1e-8;
    /**
     * Концы отрезков.
     */
    private Point start, finish;

    private final Color surfaceColor;
    
    /**
     * Координаты отрезка в проекции на плоскость XOZ.
     */
    private double a, b, c;
    
    public DoublePair getMinimumAndMaximumY() {
        double minY = Math.min(start.getY(), finish.getY()),
               maxY = Math.max(start.getY(), finish.getY());
        return new DoublePair(minY, maxY);
    }
    
    public DoublePair getMinimumAndMaximumX() {
        double minX = Math.min(start.getX(), finish.getX()),
               maxX = Math.max(start.getX(), finish.getX());
        return new DoublePair(minX, maxX);
    }
    
    public double getZOnX(double currentX) {
        DoublePair xes = getMinimumAndMaximumX();
        if (currentX <= xes.getMinValue() || xes.getMaxValue() < currentX) {
            return -Double.MAX_VALUE;
        }
        double proportion = abs((currentX - start.getX()) / (start.getX() - finish.getX()));
        double coordMin = min(start.getZ(), finish.getZ()),
                dif = abs(finish.getZ() - start.getZ());
        return coordMin + proportion * dif;
    }
    
    /**
     * Поиск точек пересечения отрезка с плоскостью Y=c.
     * Возвращает:
     * 0 точек - если отрезок не пересекается с плоскостью Y,
     * 1 точка - если отрезок пересекает плоскость Y,
     * 2 точки - если отрезок лежит на плоскости Y, то возвращается границы отрезка
     * @param currentY
     * @return 
     */
    public Point[] getIntersectionWithY(double currentY) {
        if (!isSegmentIntersectY(currentY)) {
            return null;
        }
        boolean isStartPointOnY = start.hasEqualYCoord(currentY),
                isFinishPointOnY = finish.hasEqualYCoord(currentY);
        //обе точки лежат на плоскости
        if (isStartPointOnY && isFinishPointOnY) {
            return new Point[]{start, finish};
        }
        if (isStartPointOnY) {
            return new Point[]{start};
        }
//        if (isFinishPointOnY) {
//            return new Point[]{finish};
//        }
        return new Point[]{start.getBetween(finish, currentY)};
    }
    
    private boolean isSegmentIntersectY(double currentY) {
        DoublePair pair = getMinimumAndMaximumY();
        return pair.getMinValue() <= currentY && pair.getMaxValue() >= currentY;
    }
    
    /**
     * Поиск точек пересечения отрезков.
     * Возвращает:
     * 0 точек - если отрезки не пересекаются,
     * 1 точка - если отрезки пересекаются в 1 точки,
     * 2 точки - если отрезки налагаются друг на друга, то возвращаются границы отрезка наложения
     * @param other
     * @return 
     */    
    public Point getIntersectionWithSegment(Segment other) {
        //проверка на bounding-box, если отрезки не лежат в них, то точно не пересекаются
        if (!isIntersectIn1D(other)) {
            return null;
        }
        double determinant = a * other.b - b * other.a;
        if (abs(determinant) < EPS) { //если отрезки лежат на одной прямой или параллельны
            //если они лежат на одной или параллельных прямых, то не нужно создавать новую точку
            //if (abs(this.dist(other.start)) > EPS || abs(other.dist(start)) > EPS) {
            return null;
            //}
            /*Point a, b, c, d;
            if (start.compareXAndZ(finish)) {
                a = start; b = finish;
            } else {
                b = start; a = finish;
            }
            if (other.start.compareXAndZ(other.finish)) {
                c = other.start; d = other.finish;
            } else {
                d = other.start; c = other.finish;
            }
            Point left = a.compareXAndZ(c) ? c : a, 
                  right = b.compareXAndZ(d) ? b : d;
            if (left.equals(right)) {
                return new Point[]{left};
            }
            return new Point[]{left, right};  */          
        }
        //если лежат на разных прямых, то будет 1 точка пересечения, если она лежит на каждом из отрезков
        double x = - (c * other.b - b * other.c) / determinant,
               z = - (a * other.c - c * other.a) / determinant;
        Point answer = new Point(x, 0, z);
        if (isPointBetweenEnds(answer) && other.isPointBetweenEnds(answer)) {
            return answer;
        }
        return null;
    } 
    
    private boolean isPointBetweenEnds(Point p) {
        return isBetweenCoords(start.getX(), finish.getX(), p.getX()) &&
               isBetweenCoords(start.getZ(), finish.getZ(), p.getZ());
    }
    
    private boolean isBetweenCoords(double left, double right, double value) {
        if (left > right) {
            double swap = left; left = right; right = swap;
        }
        return Double.compare(left, value) <= 0 && 
               Double.compare(value, right) <= 0;
    }
    
    private double dist(Point p) {
        return a * p.getX() + b * p.getZ() + c;
    }
    
    private boolean isIntersectIn1D(Segment other) {
        return isIntersectIn1D(start.getX(), finish.getX(), other.start.getX(), other.finish.getX()) &&
               isIntersectIn1D(start.getZ(), finish.getZ(), other.start.getZ(), other.finish.getZ());
    }
    
    private boolean isIntersectIn1D(double a, double b, double c, double d) {
        if (a > b)  {
            double e = a; a = b; b = e;
        }
	if (c > d)  {
            double e = c; c = d; d = e;
        }
	return max (a, c) <= min (b, d) + EPS;
    }
    
    public Segment(Point start, Point finish) {
        this.start = start;
        this.finish = finish;
        a = finish.getZ() - start.getZ();
        b = start.getX() - finish.getX();
        c = - a * start.getX() - b * start.getZ();
        double norm = Math.sqrt(a * a + b * b);
        if (Math.abs(norm) >= EPS) {
            a /= norm;
            b /= norm;
            c /= norm;
        }
        if (a < 0 || Math.abs(a) < EPS && b < 0) {
            a = -a;
            b = -b;
            c = -c;
        }
        surfaceColor = null;
    }

    public Segment(Point start, Point finish, Color surfaceColor) {
        this.start = start;
        this.finish = finish;
        this.surfaceColor = surfaceColor;
        a = finish.getZ() - start.getZ();
        b = start.getX() - finish.getX();
        c = - a * start.getX() - b * start.getZ();
        double norm = Math.sqrt(a * a + b * b);
        if (Math.abs(norm) >= EPS) {
            a /= norm;
            b /= norm;
            c /= norm;
        }
        if (a < 0 || Math.abs(a) < EPS && b < 0) {
            a = -a;
            b = -b;
            c = -c;
        }
    }

    public Color getSurfaceColor() {
        return surfaceColor;
    }
    
    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getFinish() {
        return finish;
    }

    public void setFinish(Point finish) {
        this.finish = finish;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Segment other = (Segment) obj;
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        return Objects.equals(this.finish, other.finish);
    }
    
    
}
