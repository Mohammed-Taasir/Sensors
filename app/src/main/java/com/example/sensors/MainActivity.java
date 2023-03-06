package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private SensorManager sensorManager;
    private List<Sensor> sensors;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.text2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        printSensors();
        specificSensor();
    }

    private void specificSensor() {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            textView2.setText("theres a heartbeat sensor here !");
        }else{
            textView2.setText("theres no heartbeat sensor here !");
        }
    }

    @SuppressLint("SetTextI18n")
    private void printSensors() {
        for(Sensor s : sensors){
            textView.setText(textView.getText()+"\n"+s.getName());
        }
    }
}