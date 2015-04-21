package com.candy.camera;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraServiceOne extends Service implements SurfaceHolder.Callback {
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private static Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    private static String TAG = "CameraOne";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.d(TAG, "on start");
        mCamera = Camera.open();
        //change sv to findViewByID
        //SurfaceView sv = new SurfaceView(getApplicationContext());

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.camera, null);
        SurfaceView sv = (SurfaceView) layout.findViewById(R.id.surfaceView);
        parameters = mCamera.getParameters();
        mCamera.setParameters(parameters);

        mCamera.startPreview();
        Log.d(TAG, "startPreview");
        sv.post(new Runnable() {
            public void run() {
                mCamera.takePicture(null, null, mCall);
            }
        });
/*
   try {
              mCamera.setPreviewDisplay(sv.getHolder());
              parameters = mCamera.getParameters();

               //set camera parameters
             mCamera.setParameters(parameters);
             mCamera.startPreview();
             Thread.sleep(1000);
             Log.d(TAG,"take pic");
             mCamera.takePicture(null, null, mCall);
             Log.d(TAG,"pic end");
             Thread.sleep(5000);
             mCamera.stopPreview();
             mCamera.release();


        } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/

        //Get a surface
        sHolder = sv.getHolder();
        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public static Camera.PictureCallback mCall = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            mCamera = null;
            Log.d(TAG, "in callback");
            //decode the data obtained by the camera into a Bitmap
       /*
         FileOutputStream outStream = null;
              try{
                  outStream = new FileOutputStream("/sdcard/Image.jpg");
                  Log   .d(TAG,"write pic");
                  outStream.write(data);
                  Log.d(TAG,"write end");
                  outStream.close();
              } catch (FileNotFoundException e){
                  Log.d("CAMERA", e.getMessage());
              } catch (IOException e){
                  Log.d("CAMERA", e.getMessage());
              }
    */

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            /*
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "MyCameraApp");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            Uri.parse("file://"+ mediaStorageDir)));
            */
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (Environment.getExternalStorageDirectory() == null) {
            Log.d("MyCameraApp", "getExternalStorageDirectory null");
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraServiceOne");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory path: " + mediaStorageDir.getPath());
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        Log.d(TAG, "write mediafile");
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
    }
}