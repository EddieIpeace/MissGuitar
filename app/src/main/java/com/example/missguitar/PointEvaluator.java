package com.example.missguitar;

import android.animation.TypeEvaluator;

public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point start = (Point) startValue;
        Point end = (Point) endValue;
        float x = start.GetX() + fraction * (end.GetX() - start.GetX());
        float y = start.GetY() + fraction * (end.GetY() - start.GetY());
        Point point = new Point(x, y);
        return point;
    }
}
