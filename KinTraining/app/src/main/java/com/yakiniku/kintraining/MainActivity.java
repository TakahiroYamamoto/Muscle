package com.yakiniku.kintraining;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yakiniku.kintraining.measure.AccManager;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // IP初期化
        this.ipaddress = "10.10.51.170";    // アドレス。とりあえず開発スペースでの値
        this.port = "50000"; // ポートは50000固定
        this.editText = ((EditText)findViewById(R.id.editTextIp));
        this.editText.setText(this.ipaddress);

        // マネージャー初期化
        this.manager = new AccManager((SensorManager)getSystemService(SENSOR_SERVICE), this.ipaddress, this.port);

        // プレイヤー情報をマネージャーから取得
        this.player = this.manager.getPlayer();
        changeButtonColor(player);

        // リスナー登録
        findViewById(R.id.buttonPlayer1).setOnClickListener(this);
        findViewById(R.id.buttonPlayer2).setOnClickListener(this);
        findViewById(R.id.buttonStartButtle).setOnClickListener(this);
        findViewById(R.id.buttonEnd).setOnClickListener(this);
        findViewById(R.id.buttonIpUpdate).setOnClickListener(this);

        // 全ボタン状態初期化
        enableButtons();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlayer1:
                // プレイヤー1に切り替え
                manager.setPlayer(AccManager.PLAYERS.P1);
                changeButtonColor(manager.getPlayer());
                break;
            case R.id.buttonPlayer2:
                // プレイヤー2に切り替え
                manager.setPlayer(AccManager.PLAYERS.P2);
                changeButtonColor(manager.getPlayer());
                break;
            case R.id.buttonStartButtle:
                // 停止ボタン以外無効化
                disableButtons();
                break;
            case R.id.buttonEnd:
                // 停止ボタンだけ無効化
                enableButtons();
            case R.id.buttonIpUpdate:
                // IPアドレス更新
                this.ipaddress = this.editText.getText().toString();
                this.manager.setIpAddress(this.ipaddress);
                Toast.makeText(this, "IP更新しました" + this.manager.getIpAddress(),  Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void changeButtonColor(AccManager.PLAYERS p){
        Button btn1 = (Button)findViewById(R.id.buttonPlayer1);
        Button btn2 = (Button)findViewById(R.id.buttonPlayer2);
        switch (p){
            case P1:
                // 1Pは黄色
                btn1.setTextColor(0xFFF5FF00);
                btn2.setTextColor(0xFFFFFFFF);
                break;
            case P2:
                // 2Pはピンク
                btn1.setTextColor(0xFFFFFFFF);
                btn2.setTextColor(0xFFFF009D);
                break;
        }
    }

    private void disableButtons(){
        findViewById(R.id.buttonPlayer1).setEnabled(false);
        findViewById(R.id.buttonPlayer2).setEnabled(false);
        findViewById(R.id.buttonStartButtle).setEnabled(false);
        findViewById(R.id.editTextIp).setEnabled(false);
        findViewById(R.id.buttonIpUpdate).setEnabled(false);
        // 停止ボタン以外無効化
        findViewById(R.id.buttonEnd).setEnabled(true);
    }

    private void enableButtons(){
        findViewById(R.id.buttonPlayer1).setEnabled(true);
        findViewById(R.id.buttonPlayer2).setEnabled(true);
        findViewById(R.id.buttonStartButtle).setEnabled(true);
        findViewById(R.id.editTextIp).setEnabled(true);
        findViewById(R.id.buttonIpUpdate).setEnabled(true);
        // 停止ボタンだけ無効化
        findViewById(R.id.buttonEnd).setEnabled(false);
    }

    /**
     * 加速度マネージャ
     */
    private AccManager manager;
    /**
     * プレイヤーID
     */
    private AccManager.PLAYERS player;
    /**
     * 接続先IPアドレス
     */
    private String ipaddress;
    private String port;
    private EditText editText;
}
