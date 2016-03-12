package watkinsalgo.Geometry;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import java.util.Arrays;

/**
 *
 * @author Евгений
 * Класс описывает точку в трехмерном пространстве.
 */
public class Point {

    private static final double eps = 1e-8;
    /**
     * Проверяет, находится ли данная точка на плоскости у=с.
     * @param yCoord
     * @return 
     */
    public boolean hasEqualYCoord(double yCoord) {
        return Math.abs(yCoord - coords[1]) <= eps;
    }

    private Segment parent;

    public Segment getParent() {
        return parent;
    }

    public void setParent(Segment parent) {
        this.parent = parent;
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
        final Point other = (Point) obj;
        if (Double.compare(coords[0] / coords[3], other.coords[0] / other.coords[3]) != 0) {
            return false;
        }
        if (Double.compare(coords[1] / coords[3], other.coords[1] / other.coords[3]) != 0) {
            return false;
        }
        return Double.compare(coords[2] / coords[3], other.coords[2] / other.coords[3]) == 0;
    }
    
    /**
     * Возвращает точку пересечения отрезка с плоскостью у=с.
     * @param other
     * @param currentY
     * @return 
     */
    public Point getBetween(Point other, double currentY) {
        double proportion = ((currentY - getY()) / (other.getY() - getY()));
        return new Point(
                getCurrentCoord(getX(), other.getX(), proportion),
                currentY,
                getCurrentCoord(getZ(), other.getZ(), proportion));
    }

    private double getCurrentCoord(double start, double finish, double proportion) {
        return start + proportion * (finish - start);
    }
    
    public boolean compareXAndZ(Point other) {
        int dif = Double.compare(getX(), other.getX());
        if (dif > 0) return false;
        if (dif < 0) return true;
        dif = Double.compare(getZ(), other.getZ());
        return dif > 0;
    }
    
    @Override
    public String toString() {
        return "X = " + coords[0] + ", Y = " + coords[1] + ", Z = " + coords[2];
    }
        
    final int DIMENSIONS = 4;
    private double[] coords = new double[]{0, 0, 0, 0};

    public Point() {
    }
    
    public Point(double x, double y, double z, double h) {
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
        coords[3] = h;
    }

    public Point(double x, double y, double z) {
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
        coords[3] = 1;
    }
    
    public Point matrixMultiplication(double[][] matrix) {
        Point res = new Point();
        for (int i = 0; i < DIMENSIONS; i++) {
            res.set(i, 0);
            for (int j = 0; j < DIMENSIONS; j++) {
                res.set(i, res.get(i) + get(j) * matrix[i][j]);
            }
        }
        return res;
    }
    
    /**
     * @return Х-координату точки
     */
    public double getX() {
        return coords[0];
    }
    
    /**
     * @return Y-координату точки
     */
    public double getY() {
        return coords[1];
    }
    
    /**
     * @return Z-координату точки
     */
    public double getZ() {
        return coords[2];
    }
    
    /**
     * Возвращает координату точки по индексу.
     * @param i - индекс
     * @return координата точки
     */
    public double get(int i) {
        return coords[i];
    }
    
    /**
     * @param x - новое значение Х 
     */
    public void setX(double x) {
        coords[0] = x;
    }
    
    /**
     * @param y - новое значение Х 
     */
    public void setY(double y) {
        coords[1] = y;
    }
    
    /**
     * @param z - новое значение z
     */
    public void setZ(double z) {
        coords[2] = z;
    }
    
    /**
     * 
     * @param i - индекс кооординаты
     * @param value - новое значение координаты
     */
    public void set(int i, double value) {
        coords[i] = value;
    }
    
    /**
     * Копирование точки.
     * @param p - образец
     */
    public void set(Point p) {
        for (int i = 0; i < p.DIMENSIONS; i++) {
            coords[i] = p.coords[i];
        }
    }
}
