package com.example.missguitar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

public class MyAnimView extends View {
    public static final float kRadius = 50f;
    private Point current_point;
    private Paint paint;

    public MyAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == current_point) {
            current_point = new Point(kRadius, kRadius);
            drawCirCle(canvas);
            startAnimation();
        } else {
            drawCirCle(canvas);
        }
    }

    private void drawCirCle(Canvas canvas) {
        float x = current_point.GetX();
        float y = current_point.GetY();
        canvas.drawCircle(x, y, kRadius, paint);
    }

    private void startAnimation() {
        Point start_point = new Point(kRadius, kRadius);
        Point end_point = new Point(getWidth() - kRadius, getHeight() - kRadius);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), start_point, end_point);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                current_point = (Point) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(5000);
        anim.start();
    }
}
