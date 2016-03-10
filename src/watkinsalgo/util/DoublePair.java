/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo.util;

/**
 * Класс, запоминающий минимальное и максимальное значение Y в отрезке и плоскости.
 * @author Евгений
 */
public class DoublePair {
    private double minValue, maxValue;

    public DoublePair(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public DoublePair(DoublePair first, DoublePair second) {
        minValue = Math.min(first.minValue, second.minValue);
        maxValue = Math.max(first.maxValue, second.maxValue);
    }

    public void changeValue(DoublePair other) {
        minValue = Math.min(minValue, other.minValue);
        maxValue = Math.max(maxValue, other.maxValue);
    }
    
    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double mxValue) {
        this.maxValue = mxValue;
    }

    @Override
    public boolean equals(Object obj) {
        DoublePair pair = (DoublePair) obj;
        return Double.compare(minValue, pair.minValue) == 0 && Double.compare(maxValue, pair.maxValue) == 0;
    }

    
}
