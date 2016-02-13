package com.yakiniku.kintraining;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yakiniku.kintraining.connect.ServerConnector;

import java.net.URL;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // testServerConnector();
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
     * サーバコネクトテストドライバ
     * 山田以外はスルーでお願いします
     * Yahoo相手にGETでレスポンス200、HTMLの応答確認済み
     */
    private void testServerConnector(){

        ServerConnector serverConnector = new ServerConnector();
        URL url = null;
        try {
            url = new URL("https://www.google.co.jp/");
        }catch (java.net.MalformedURLException e){
            Log.v("Main", "URL Error");
        }

        serverConnector.execute(url);

    }
}
