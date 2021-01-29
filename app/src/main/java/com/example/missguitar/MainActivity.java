package com.example.missguitar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView text_view;
    private Button button_start;
    private int pitch = 0;
    private TimerTask task_17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_view = (TextView) findViewById(R.id.text_view);
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode();
            }
        });
        task_17 = new TimerTask() {
            @Override
            public void run() {
                pitch = pitch + 1;
                if (pitch > 7) {
                    pitch = 1;
                }
                Message msg = new Message();
                msg.arg1 = pitch;
                handler.sendMessage(msg);
            }
        };
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            text_view.setText(String.valueOf(msg.arg1));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task_17.cancel();
    }

    private void mode() {
        pitch = 1;
        String str = String.valueOf(pitch);
        text_view.setText(str);
        Timer timer = new Timer();
        timer.schedule(task_17, 1000, 1000);
    }
}