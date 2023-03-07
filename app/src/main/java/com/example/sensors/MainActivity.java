package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xtextView;
    private TextView ytextView;
    private TextView ztextView;
    private SensorManager sensorManager;
    private Sensor acceleroSensor;
    private boolean hasAcceleroSensor;
    private Vibrator vibrator;
    private float currX, currY, currZ, oldX, oldY, oldZ;
    boolean isitFirstTime = true;
    private float xDiff, yDiff, zDiff;
    private float shakeThreshold = 5f;



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xtextView = findViewById(R.id.text);
        ytextView = findViewById(R.id.text2);
        ztextView = findViewById(R.id.text3);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            hasAcceleroSensor = true;
        }else{
            xtextView.setText("Accelerometer sensor ain't here");
            hasAcceleroSensor = false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xtextView.setText(sensorEvent.values[0]+" m/s²");
        ytextView.setText(sensorEvent.values[1]+" m/s²");
        ztextView.setText(sensorEvent.values[2]+" m/s²");

        currX = sensorEvent.values[0];
        currY = sensorEvent.values[1];
        currZ = sensorEvent.values[2];

        if(!isitFirstTime){
            xDiff = Math.abs(oldX-currX);
            yDiff = Math.abs(oldY-currY);
            zDiff = Math.abs(oldZ-currZ);

            if((xDiff>shakeThreshold && yDiff>shakeThreshold) || (yDiff>shakeThreshold && zDiff>shakeThreshold) || (zDiff>shakeThreshold && xDiff>shakeThreshold)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(MainActivity.this);
                    tv.setTextSize(12);
                    tv.setText("..Shake Alert.. \n\nxDiff: "+xDiff+"\n"+"yDiff: "+yDiff+"\n"+"zDiff: "+zDiff+"\n");
                    toast.setView(tv);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    vibrator.vibrate(500);
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(MainActivity.this);
                    tv.setTextSize(12);
                    tv.setText("..Shake Alert.. \n\nxDiff: "+xDiff+"\n"+"yDiff: "+yDiff+"\n"+"zDiff: "+zDiff+"\n");
                    toast.setView(tv);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        }

        oldX = currX;
        oldY = currY;
        oldZ = currZ;

        isitFirstTime = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasAcceleroSensor) sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(hasAcceleroSensor) sensorManager.unregisterListener(this);
    }
}