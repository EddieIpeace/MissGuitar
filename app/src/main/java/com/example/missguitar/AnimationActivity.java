package com.example.missguitar;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnimationActivity extends AppCompatActivity {
    private String TAG = "eddie";
    private Button button_animation;
    private TextView text_view_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        text_view_show = (TextView) findViewById(R.id.text_view_show);
        button_animation = (Button) findViewById(R.id.button_animation);
        button_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point point_start = new Point(0, 0);
                Point point_end = new Point(300, 300);
                ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), point_start, point_end);
                anim.setDuration(5000);
                anim.start();
            }
        });
    }
}

