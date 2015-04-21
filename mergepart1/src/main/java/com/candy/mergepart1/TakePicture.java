package com.candy.mergepart1;
/**
 * 用来拍照的Activity
 * @author Chenww
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class TakePicture extends Activity {
    private PowerManager.WakeLock wakeLock = null;
    private SurfaceView mySurfaceView;
    private SurfaceHolder myHolder;
    private Camera myCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera);

        Log.d("Demo", "oncreate");

        initSurface();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initCamera();
            }
        }).start();

    }


    private void initSurface()
    {
        mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        myHolder = mySurfaceView.getHolder();
        myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }
    public void acquireWakeLock(Context context){
        if(wakeLock == null){
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"my tag");
            wakeLock.acquire();
        }
    }
    public void releaseWakeLock(){
        if(wakeLock != null && wakeLock.isHeld()){
            wakeLock.release();
            wakeLock = null;
        }
    }

    private void initCamera() {

        //如果存在摄像头
        if(checkCameraHardware(getApplicationContext()))
        {
            //获取摄像头（首选前置，无前置选后置）
            if(openFacingFrontCamera())
            {
                Log.d("Demo", "openCameraSuccess");
                //进行对焦
                autoFocus();

            }
            else {
                Log.d("Demo", "openCameraFailed");
            }

        }
    }

    private void autoFocus() {

       try {
           Thread.sleep(1000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

        //自动对焦
        myCamera.autoFocus(myAutoFocus);
        Log.d("Demo", "ready to take picture");
        //对焦后拍照
        myCamera.takePicture(null, null, myPicCallback);
    }

    @Override
    protected void onResume() {
        acquireWakeLock(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        releaseWakeLock();
        super.onPause();
    }

    //判断是否存在摄像头
    private boolean checkCameraHardware(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // 设备存在摄像头
            return true;
        } else {
            // 设备不存在摄像头
            return false;
        }

    }

    //得到后置摄像头
    private boolean openFacingFrontCamera() {

        //尝试开启前置摄像头
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    Log.d("Demo", "tryToOpenCamera");
                    myCamera = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        //如果开启前置失败（无前置）则开启后置
        if (myCamera == null) {
            for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        myCamera = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        return false;
                    }
                }
            }
        }

        try {
            //这里的myCamera为已经初始化的Camera对象
            myCamera.setPreviewDisplay(myHolder);
        } catch (IOException e) {
            e.printStackTrace();
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }

        myCamera.startPreview();

        return true;
    }

    //自动对焦回调函数(空实现)
    private AutoFocusCallback myAutoFocus = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    //拍照成功回调函数
    private PictureCallback myPicCallback = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("Demo", "hui diao le" );
            //完成拍照后关闭Activity
            TakePicture.this.finish();

            //将得到的照片进行270°旋转，使其竖直
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.preRotate(270);
            bitmap = Bitmap.createBitmap(bitmap ,0,0, bitmap .getWidth(), bitmap .getHeight(),matrix,true);

            //创建并保存图片文件
            isDirExist();
            File pictureFile = new File(getApplicationContext().getString(R.string.file_path), Tool.generateSequenceNo()+".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception error) {
                Toast.makeText(TakePicture.this, "拍照失败", Toast.LENGTH_SHORT).show();;
                Log.d("Demo", "保存照片失败" + error.toString());
                error.printStackTrace();
                myCamera.stopPreview();
                myCamera.release();
                myCamera = null;
            }

            Log.d("Demo", "获取照片成功");
            Toast.makeText(TakePicture.this, "获取照片成功", Toast.LENGTH_SHORT).show();;
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;

        }
    };
    public void isDirExist(){
        File file = new File(getApplicationContext().getString(R.string.file_path));
        if(!file.exists())
            file.mkdir();  //如果不存在则创建
    }

}
