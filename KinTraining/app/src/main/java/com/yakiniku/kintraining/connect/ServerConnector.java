package com.yakiniku.kintraining.connect;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnector extends AsyncTask<URL, Void, String> {

    private final String TAG = "ServerConnector";

    /**
     * HTTPリクエスト開始メソッド
     * 今のところ、指定URLに何もパラメータをつけずにただのGETするだけ
     * @param url 入力URL
     * @return 応答文字列
     */
    @Override
    protected String doInBackground(URL... url) {

        String resultString = "";
        HttpURLConnection conn = null;
        try {
            Log.v(TAG, url[0].toString());
            conn = (HttpURLConnection) url[0].openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int res = conn.getResponseCode();
            Log.v(TAG, Integer.toString(res));

            resultString = streamToString(conn.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return resultString;
    }

    /**
     * HTTPリクエスト終了後の処理。今のところ特に何もしない
     * @param resp
     */
    @Override
    protected void onPostExecute(String resp){

        Log.v("ServerConnector", resp);
    }

    /**
     * ストリームから文字列に変換するサブメソッド
     * @param stream
     * @return
     * @throws IOException
     */
    private String streamToString(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        try {
            stream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
