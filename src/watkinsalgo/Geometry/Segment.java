/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo.Geometry;

import java.util.Objects;
import watkinsalgo.util.DoublePair;

/**
 *
 * @author Евгений
 */
public class Segment {
    /**
     * Концы отрезков
     */
    private Point start, finish;

    public DoublePair getMinimumAndMaximumY() {
        double minY = Math.min(start.getY(), finish.getY()),
               maxY = Math.max(start.getY(), finish.getY());
        return new DoublePair(minY, maxY);
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
        if (isFinishPointOnY) {
            return new Point[]{finish};
        }
        return new Point[]{start.getBetween(finish, currentY)};
    }
    
    private boolean isSegmentIntersectY(double currentY) {
        DoublePair pair = getMinimumAndMaximumY();
        return pair.getMinValue() <= currentY && pair.getMxValue() >= currentY;
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
    public Point[] getIntersectionWithSegment(Segment other) {
        
    } 
    
    public Segment(Point start, Point finish) {
        this.start = start;
        this.finish = finish;
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
