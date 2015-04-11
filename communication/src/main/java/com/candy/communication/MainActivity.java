package com.candy.communication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login("ssss","2324","http://192.168.1.121/index.php/Form/insert");
//        new postAsyncTask().execute();
    }
    class postAsyncTask extends AsyncTask<Void, Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
           // Post();
//            uploadfile("http://192.168.199.160:8080/android/getData.php","/sdcard/test.png");

            return null;
        }
    }
    public void Post(){
        try {
            URL url = new URL("http://192.168.199.160:8080/android/getData.php");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoOutput(true);
            String data = "username= " + URLEncoder.encode("你好","UTF-8")
                    + "password= " + URLEncoder.encode("你好","UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();
            if(urlConnection.getResponseCode()==200){
                InputStream in = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while((len = in.read(buffer))!=-1){
                    baos.write(buffer,0,len);
                }
                in.close();
                baos.close();
                final String result = new String(baos.toByteArray());
                System.out.println(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void login(String username, String password,String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);
        client.post(url,params,new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

            }
        });
    }
    public void uploadfile(String url,String filepath){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("pciture",new File(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
