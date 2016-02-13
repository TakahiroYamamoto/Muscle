package com.yakiniku.kintraining;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yakiniku.kintraining.connect.ServerConnector;
import com.yakiniku.kintraining.measure.AccManager;

import java.net.URL;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // testServerConnector();

        this.manager = new AccManager((SensorManager)getSystemService(SENSOR_SERVICE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 加速度マネージャ
     */
    private AccManager manager;

    /**
     * サーバーアクセス用フィールド
     * （とりあえず、ここでいいのか・・・？）
     */
    private final String SERVER_URL = "http://10.10.55.237:1000/";
    private final String DEVICE1    = "device1";
    private final String DEVICE2    = "device2";

    /**
     * サーバコネクトテストドライバ兼HowToUse
     * サーバのカウントアップ確認済み
     * とりあえず onCreate から呼んでいます
     */
    private void testServerConnector(){

        ServerConnector serverConnector = new ServerConnector();

        // アクセス先URL作成
        String dev1 = SERVER_URL + DEVICE1;
        String dev2 = SERVER_URL + DEVICE2;

        URL url = null;
        try {
            url = new URL(dev1);
        }catch (java.net.MalformedURLException e){
            Log.v("Main", "URL Error");
        }

        // 対象デバイスのカウントアップリクエスト開始
        serverConnector.execute(url);
    }
}
