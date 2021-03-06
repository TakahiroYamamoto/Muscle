package com.yakiniku.kintraining.measure;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.yakiniku.kintraining.connect.ServerConnector;

import org.apache.http.conn.scheme.PlainSocketFactory;

import java.net.URL;
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

    /**
     * プレイヤー情報
     */
    public enum PLAYERS { P1, P2 }
    private PLAYERS player;
    public void setPlayer(PLAYERS p){
        player = p;
    }
    public PLAYERS getPlayer(){ return player; }

    /**
     * IPアドレス
     */
    private String ipaddress;
    public void setIpAddress(String ip){
        ipaddress = ip;
        Log.d("TAG", ipaddress);
    }
    public String getIpAddress(){ return ipaddress; }

    /**
     * ポート番号
     */
    private String port;

    public AccManager(SensorManager manager, String ip, String po) {
        ax = ay = az = 0;
        mx = my = mz = 0;
        vx = vy = vz = 0;
        counter = 0;

        this.manager = manager;

        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        // 生成時点ではプレイヤー1固定とする
        player = PLAYERS.P1;

        // とりあえず初期値
        ipaddress = ip;
        port = po;
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

            double naiseki = ax*mx + ay*my + az*mz;
            double len_a = Math.sqrt(ax*ax + ay*ay + az*az);
            double len_b = Math.sqrt(mx*mx + my*my + mz*mz);

            mx = ax;
            my = ay;
            mz = az;

            if(len_a == 0 || len_b == 0){
                return;
            }
            double gaiseki = naiseki / ( len_a * len_b );
            double kakudo = Math.acos(gaiseki) * 180 / 3.14;

            // Log.d("TAG", "角度 : " + kakudo);

            if(90.0 < kakudo) { // 前回から加速度の方向が大きく変わった時
                Log.d("TAG", "count up : " + counter);
                counter++;

                // サーバーカウントアップ
                serverCountUp();
            }
        }
    }

    private void serverCountUp(){
        String SERVER_URL       = "http://" + ipaddress +  ":" + port +  "/";
        final String DEVICE1    = "device1";
        final String DEVICE2    = "device2";

        ServerConnector serverConnector = new ServerConnector();

        // アクセス先URL作成
        switch (player){
            case P1:
                SERVER_URL += DEVICE1;
                break;
            case P2:
                SERVER_URL += DEVICE2;
                break;
        }

        URL url = null;
        try {
            url = new URL(SERVER_URL);
        } catch (java.net.MalformedURLException e) {
            Log.v("Main", "URL Error");
        }

        // 対象デバイスのカウントアップリクエスト開始
        serverConnector.execute(url);
    }
}