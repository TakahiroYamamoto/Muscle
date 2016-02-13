package com.yakiniku.kintraining.measure;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

/**
 * Created by yamamoto on 16/02/13.
 */
public class AccManager  implements SensorEventListener{
    private SensorManager manager;

    AccManager(SensorManager manager) {
        this.manager = manager;
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
            Log.d("TAG","" + event.values[SensorManager.DATA_X] + " " + event.values[SensorManager.DATA_Y] + " " + event.values[SensorManager.DATA_Z]);
            }
        }
}
