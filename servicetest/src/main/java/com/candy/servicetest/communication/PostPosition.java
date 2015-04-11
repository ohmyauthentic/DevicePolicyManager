package com.candy.servicetest.communication;
import com.candy.servicetest.util.Point;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2015/3/30.
 */
public class PostPosition {
    public static void postData(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.199.160:8080/android/getData.php");
        HttpParams httpParams = new BasicHttpParams();
        List nameValuePair = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        Point point = new Point();
        point.setDate(new Date());
        point.setLatitude(100);
        point.setLongtitude(200);
        try {
            jsonObject.put("point", point);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameValuePair.add(new BasicNameValuePair("jsonString",jsonObject.toString()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setParams(httpParams);
        try {
            HttpResponse response =  httpClient.execute(httpPost);
            String rev = EntityUtils.toString(response.getEntity());
            System.out.println(rev);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
