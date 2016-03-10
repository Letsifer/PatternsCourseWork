package watkinsalgo.Geometry;

import static java.lang.Math.abs;
import static java.lang.Math.min;

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
    
    /**
     * Возвращает точку пересечения отрезка с плоскостью у=с.
     * @param other
     * @param currentY
     * @return 
     */
    public Point getBetween(Point other, double currentY) {
        double proportion = abs((currentY - getY()) / (getY() - other.getY()));
        return new Point(
                getCurrentCoord(getX(), other.getX(), proportion),
                currentY,
                getCurrentCoord(getZ(), other.getZ(), proportion));
    }

    private double getCurrentCoord(double start, double finish, double proportion) {
        double coordMin = min(start, finish),
                dif = abs(finish - start);
        return coordMin + proportion * dif;
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
