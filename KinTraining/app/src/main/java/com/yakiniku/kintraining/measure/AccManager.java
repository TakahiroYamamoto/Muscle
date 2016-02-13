package com.yakiniku.kintraining.measure;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.yakiniku.kintraining.connect.ServerConnector;

import java.util.List;

/**
 * Created by yamamoto on 16/02/13.
 */
public class AccManager  implements SensorEventListener{
    private SensorManager manager;
    private float ax,ay,az;
    private float mx,my,mz;
    private float vx,vy,vz;
    private int counter;
    private ServerConnector connector;

    public AccManager(SensorManager manager , ServerConnector connector) {
        ax = ay = az = 0;
        mx = my = mz = 0;
        vx = vy = vz = 0;
        counter = 0;

        this.manager = manager;
        this.connector = connector;

        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        // Listenerの登録解除
        this.manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[SensorManager.DATA_X];
            ay = event.values[SensorManager.DATA_Y];
            az = event.values[SensorManager.DATA_Z];
            vz = az - mz;

            if(vz < -15.0 || 15.0 < vz) { // 速度が大きい時
                Log.d("TAG", "count up : " + counter);
                counter++;
            }
            mx = ax;
            my = ay;
            mz = az;
        }
    }
}