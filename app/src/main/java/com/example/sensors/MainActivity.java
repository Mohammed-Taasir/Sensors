package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.PixelCopy;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textViewStepCounter;
    private TextView textViewStepDetector;

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private Sensor stepDetector;
    private boolean hasStepCounter;
    private boolean hasStepDetecter;
    private Vibrator vibrator;

    // works in your oppo phone not xiaomi phone.

    int stepCount = 0, stepDetect = 0;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);       // this app works only while keeping screen on.
        textViewStepCounter = findViewById(R.id.text2);
        textViewStepDetector = findViewById(R.id.text4);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            hasStepCounter = true;
        }else{
            textViewStepCounter.setText("Step counter sensor ain't here");
            hasStepCounter = false;
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            hasStepDetecter = true;
        }else{
            textViewStepCounter.setText("Step detector sensor ain't here");
            hasStepDetecter = false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == stepCounter){
            stepCount = (int) sensorEvent.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));
        }
        else if(sensorEvent.sensor == stepDetector){
            stepDetect = (int) (stepDetect + sensorEvent.values[0]);            // it will keep adding when detected.
            textViewStepDetector.setText(String.valueOf(stepDetect));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasStepCounter) sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        if(hasStepDetecter) sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(hasStepCounter) sensorManager.unregisterListener(this, stepCounter);
        if(hasStepDetecter) sensorManager.unregisterListener(this, stepDetector);
    }
}