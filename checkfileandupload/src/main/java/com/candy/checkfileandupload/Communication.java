package com.candy.checkfileandupload;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by candy on 2015/4/9.
 */
@SuppressWarnings("ALL")
public class Communication {
    public static final String UPLOAD_URL = "http://192.168.1.101:8080/android/getData.php";
    public static void uploadfile(String url,File file){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("pciture",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(url,params,new TextHttpResponseHandler() {


            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                System.out.println("失败:"+s);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                if(s!=null){
                    File temp = new File("/sdcard/phone/"+s);
                    Tool.delete(temp);
                    System.out.println("删除:"+s);
                }
                System.out.println("上传:"+s);
            }
        });
    }
}
