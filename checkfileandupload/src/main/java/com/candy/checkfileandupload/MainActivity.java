package com.candy.checkfileandupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File filepath = new File("/sdcard/Pictures");
        Tool.delete(filepath);
/*        for(File f:filepath.listFiles()){
            Communication.uploadfile(Communication.UPLOAD_URL,f);
        }*/
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                socketConnect();
            }
        });*/
        // new NetworkTask().execute();
    }

    private class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            socketConnect();
            return null;
        }
    }

    public void socketConnect() {
        try {
            System.out.println("准备连接");
            Socket socket = null;
            socket = new Socket("192.168.1.114", 2046);
            System.out.println("连接上了");
            InputStream inputStream = socket.getInputStream();
            byte buffer[] = new byte[1024 * 4];
            int temp = 0;
            String res = null;
            //从inputstream中读取客户端所发送的数据
            System.out.println("接收到服务器的信息是：");


            while ((temp = inputStream.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, temp));
                res += new String(buffer, 0, temp);
            }
            System.out.println("已经结束接收信息……");

            socket.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
