/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo.Geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import watkinsalgo.util.DoublePair;

/**
 *
 * @author Евгений
 */
public class Surface {
    private ArrayList<Segment> edges = new ArrayList<>();
    private final Color surfaceColor;
    
    /**
     * Находит наименьшее и наибольшее значени Y у плоскости.
     * @return 
     */
    public DoublePair getMinimumAndMaximumY() {
        DoublePair answer = new DoublePair(Double.MAX_VALUE, Double.MIN_VALUE);
        edges.stream().forEach(current -> answer.changeValue(current.getMinimumAndMaximumY()));
        return answer;
    }
    
    /**
     * Нахождение точки или отрезка пересечения данной плоскости с плоскостью Y=c.
     * Если только одна точка пересечения, то обе границы одинаковы.
     * @param currentY
     * @return 
     */
    public Segment getIntersectionWithY(double currentY) {
        Point start = null, finish = null;
        if (checkForOneY(currentY)) {
            start = new Point(Double.MAX_VALUE, 0, 0);
            finish = new Point(Double.MIN_VALUE, 0, 0);
            for (Segment edge : edges) {
                if (edge.getStart().getX() < start.getX()) {
                    start = edge.getStart();                    
                }
                if (edge.getFinish().getX() < start.getX()) {
                    start = edge.getFinish();                    
                }
                if (edge.getStart().getX() > finish.getX()) {
                    finish = edge.getStart();
                }
                if (edge.getFinish().getX() > finish.getX()) {
                    finish = edge.getFinish();
                }
            }
        } else {
            for (Segment edge : edges) {
                Point[] intersection = edge.getIntersectionWithY(currentY);
                if (intersection != null) {
                    if (intersection.length == 2) {
                        start = intersection[0];
                        finish = intersection[1];
                        break;
                    }
                    if (start == null) {
                        start = intersection[0];
                    } else {
                        if (start.equals(intersection[0])) {
                            continue;
                        }
                        finish = intersection[0];
                        break;
                    }
                }
            }
            if (finish == null) {
                finish = start;
            }
        }
        if (start == null || finish == null) {
            return null;
        }
        Segment segment = new Segment(start, finish, surfaceColor);
        start.setParent(segment);
        finish.setParent(segment);
        return segment;
    }

    private boolean checkForOneY(Double currentY) {
        Double y0 = edges.get(0).getStart().getY(),
               y1 = edges.get(0).getFinish().getY(),
               y2 = edges.get(1).getFinish().getY();
        return Double.compare(currentY, y0) == 0 &&
               Double.compare(y0, y1) == 0 &&
               Double.compare(y1, y2) == 0;
    }
    
    public Surface(Color surfaceColor) {
        this.surfaceColor = surfaceColor;
    }
    
    public void addEdge(Point a, Point b) {
        edges.add(new Segment(a, b));
    }

    public Color getSurfaceColor() {
        return surfaceColor;
    }
    
    public int getEdgesNumber() {
        return edges.size();
    }
    
    /**
     * Матрица поворота.
     */
    private double[][] rotationMatrix = new double[][] 
    {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
    };
    
    /**
     * Матрица переноса (по умолчанию сдвига нет);
     * для сдвига необходимо изменить элементы:
     * [3][0] - по оси OX
     * [3][1] - по оси OY
     * [3][2] - по оси OZ.
     */
    private double[][] translationMatrix = new double[][]
    {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
    };
    
    /**
     * Матрица растяжения (по умолчанию коэффициенты равны 1);
     * для растяжения необходимо ихменить элементы:
     * [0][0] - по оси OX
     * [1][1] - по оси OY
     * [2][2] - по оси OZ.
     */
    private double[][] dialationMatrix = new double[][]
    {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
    };
    
    public void dialation(double dX, double dY, double dZ) {
        dialationMatrix[0][0] = dX;
        dialationMatrix[1][1] = dY;
        dialationMatrix[2][2] = dZ;
        for (int i = 0; i < edges.size(); i++) {
            Segment current = edges.get(i);
            current.setStart(current.getStart().matrixMultiplication(dialationMatrix));
            current.setFinish(current.getFinish().matrixMultiplication(dialationMatrix));
        }
//        projectFigureOnXOY();
    }
    
    private void setTranslationParameters(double x, double y, double z) {
        translationMatrix[0][3] = x;
        translationMatrix[1][3] = y;
        translationMatrix[2][3] = z;
    }
    
    public void translation(double dx, double dy, double dz) {
        setTranslationParameters(dx, dy, dz);
        for (int i = 0; i < edges.size(); i++) {
            Segment current = edges.get(i);
            current.setStart(current.getStart().matrixMultiplication(translationMatrix));
            current.setFinish(current.getFinish().matrixMultiplication(translationMatrix));          
        }
//        projectFigureOnXOY();
    }
    
    private void rotate(int i, int j, double angle) {
        angle = toRadians(angle);
        if (i > j) {
            int c = i; i = j; j = c;
        }
        rotationMatrix[i][i] = rotationMatrix[j][j] = cos(angle);
        rotationMatrix[i][j] = -sin(angle);
        rotationMatrix[j][i] = sin(angle);
        for (int k = 0; k < edges.size(); k++) {
            Segment current = edges.get(k);
            current.setStart(current.getStart().matrixMultiplication(rotationMatrix));
            current.setFinish(current.getFinish().matrixMultiplication(rotationMatrix));
        }
        rotationMatrix[i][i] = rotationMatrix[j][j] = 1;
        rotationMatrix[i][j] = rotationMatrix[j][i] = 0;
//        projectFigureOnXOY();
    }
    
    public void XRotate(double angle) {
        rotate(1, 2, angle);
    }
    
    public void YRotate(double angle) {
        rotate(0, 2, angle);
    }
 
    public void ZRotate(double angle) {
        rotate(0, 1, -angle);
    }

    private double alpha = toRadians(27), betta = toRadians(45);
    /**
     * Матрица проецирования 3D-объекта на 2D-изображение на экране.
     */
    /*private final double[][] projectionMatrix = new double[][]{
        {cos(alpha), -sin(alpha), 0, 0},
        {0, 0, -1, 500},
        {sin(alpha), cos(alpha), 0, 0},
        {0, 0, 0, 1}
    };

    private void projectFigureOnXOY() {
        for (int i = 0; i < edges.size(); i++) {
            Segment e = edges.get(i);
            projections[i] = new Point
                (vertexes[i].getX(), 
                 vertexes[i].getZ(), 
                 vertexes[i].getY());
            projections[i] = projections[i].matrixMultiplication(projectionMatrix);
            double X = projections[i].getX() * (2000 - 700) / (2000 - projections[i].getZ());
            double Y = projections[i].getY() * (2000 - 700) / (2000 - projections[i].getZ());
            projections[i].setX(2 * X );
            projections[i].setY(2 * (Y - 325));
            projections[i].setZ(0);
        }       
    }*/
}
