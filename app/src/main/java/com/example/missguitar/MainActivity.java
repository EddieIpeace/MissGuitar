package com.example.missguitar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {
    private TextView text_view;
    private TextView text_view_pitch;
    private Button button_start;
    private Button button_switch;
    private int pitch = 0;
    private TimerTask timer_task;
    private boolean is_running;
    private String TAG = "eddie";
    private int mode = 1;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private AudioDispatcher audio_dispatcher;
    private PitchDetectionHandler pitch_handler;
    private AudioProcessor audio_processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        is_running = false;
        pitch = 1;
        String str = String.valueOf(pitch);
        text_view = (TextView) findViewById(R.id.text_view);
        text_view.setText(str);
        text_view_pitch = (TextView) findViewById(R.id.text_view_pitch);
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setText("开始");
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_running) {
                    is_running = false;
                    button_start.setText("开始");
                    timer_task.cancel();
                    spinner.setEnabled(true);
                } else {
                    is_running = true;
                    button_start.setText("结束");
                    InitTimerTask();
                    Timer timer = new Timer();
                    timer.schedule(timer_task, 1000, 1000);
                    spinner.setEnabled(false);
                }
            }
        });
        button_switch = (Button) findViewById(R.id.button_switch);
        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnimationActivity.class);
                startActivity(intent);
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner_mode);
        data_list = new ArrayList<String>();
        data_list.add("顺序");
        data_list.add("随机");
        arr_adapter = new ArrayAdapter<>(this, R.layout.spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) spinner.getSelectedItem();
                if ("顺序" == str) {
                    mode = 1;
                } else if ("随机" == str) {
                    mode = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        audio_dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        pitch_handler = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                final float pitch_in_hz = pitchDetectionResult.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       text_view_pitch.setText("" + pitch_in_hz);
                    }
                });
            }
        };
        audio_processor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pitch_handler);
        audio_dispatcher.addAudioProcessor(audio_processor);
        new Thread(audio_dispatcher, "Audio Dispatcher").start();
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
        timer_task.cancel();
    }
    private void InitTimerTask() {
        switch (mode) {
            case 1:
                InitTimerTaskSequence();
                break;
            case 2:
                InitTimerTaskRandom();
                break;
        }
    }

    private void InitTimerTaskSequence() {
        timer_task = new TimerTask() {
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
    private void InitTimerTaskRandom() {
        timer_task = new TimerTask() {
            @Override
            public void run() {
                pitch = GetRandom(1, 7);
                Message msg = new Message();
                msg.arg1 = pitch;
                handler.sendMessage(msg);
            }
        };
    }
    private void CheckTimerTask(TimerTask task) {
        if (null == task) {
            Log.e(TAG, "CheckTimerTask: null");
        } else {
            Log.e(TAG, "CheckTimerTask: not null");
        }
    }
    private int GetRandom(int start, int end) {
        return (int)(Math.random() * (end - start + 1) + start);
    }
}