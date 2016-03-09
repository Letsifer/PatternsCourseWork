/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo.Geometry;

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
}
