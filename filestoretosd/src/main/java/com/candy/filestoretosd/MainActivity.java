package com.candy.filestoretosd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private ImageView imageVIew;
    private Button downPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageVIew = (ImageView) findViewById(R.id.imageView);
        downPic = (Button) findViewById(R.id.down_pic);
        downPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://b.hiphotos.baidu.com/image/pic/item/d53f8794a4c27d1eabfb59cd19d5ad6edcc438dd.jpg";
                new DownImgAsyncTask().execute(url);
            }
        });
    }
    class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
           Bitmap b = getImageBitmap(params[0]);
            return b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageVIew.setImageBitmap(null);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                imageVIew.setImageBitmap(bitmap);
                try {
                    saveBitmap("test",bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection coon = (HttpURLConnection) imgUrl.openConnection();
            coon.setDoInput(true);
            coon.connect();
            InputStream is = coon.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void saveBitmap(String bitmapName, Bitmap bitmap) throws IOException {
        File f = new File("/sdcard/"+bitmapName+".png");
        f.createNewFile();
        FileOutputStream fout = null;
        fout = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
        fout.flush();
        fout.close();
    }
}
