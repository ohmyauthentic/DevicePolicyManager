package com.candy.mergepart1;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.Date;

/**
 * Created by candy on 2015/4/9.
 */
public class Communication {
    public static void post(String username, String password,String url){
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
    public static void registerPhone(String username, String password,String url){
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
    public static void post(BDLocation location,String url, final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        SharedPreferences sharedPreferences = context.getSharedPreferences("phoneState", Context.MODE_PRIVATE);
        String imei = sharedPreferences.getString("imei","0");
        params.put("latitude",location.getLatitude());
        params.put("longtitude",location.getLongitude());
        params.put("time",location.getTime());
        params.put("imei",imei);
        params.put("address",location.getAddrStr());
        Toast.makeText(context,location.getTime()+imei+location.getLatitude()+":"+location.getLongitude(),Toast.LENGTH_SHORT).show();
        client.post(url,params,new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(context,"发送失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Toast.makeText(context,"发送成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
