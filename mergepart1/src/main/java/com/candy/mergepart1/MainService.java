package com.candy.mergepart1;

import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.List;


public class MainService extends Service {
    public static final String INTERNAL_ACTION_MAIN = "sun.phone.main";
    private CodeReceiver receiver;
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private Intent BaiduServiceIntent;
    private MediaPlayer mp;
    private SharedPreferences sharedPreferences;
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class CodeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("code");
            boolean send = intent.getBooleanExtra("send",false);
            String sender = intent.getStringExtra("sender");
            if(content.contains("Baidu")){
                if(!MainService.isServiceRunning(context,"BaiduMapService")){
                   startBaiduService();
                };
                broadcast2BaiduService(sender,send,content,context);
            }else if(content.contains("Alert")){
                controlAlert(content);
            }else if(content.contains("lock")){
                lockScreen();
            }
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        IntentFilter filter = new IntentFilter(INTERNAL_ACTION_MAIN);
        receiver = new CodeReceiver();
        registerReceiver(receiver,filter);
        //active();//激活设备管理器
        //getPhoneState();//获取手机状态信息
    }
    public void active(){
        Toast.makeText(getApplicationContext(),"active", Toast.LENGTH_SHORT).show();
        if(dpm.isAdminActive(cpn)) {}else{
            Toast.makeText(getApplicationContext(),"active2", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //权限列表
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cpn);
            //描述(additional explanation)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");
            startActivity(intent);
        }
    }

    public void startBaiduService(){
        BaiduServiceIntent = new Intent(getApplicationContext(), BaiduMapService.class);
        BaiduServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(BaiduServiceIntent);
    }
    public void stopBaiduService(){
        stopService(BaiduServiceIntent);
    }
    public void controlAlert(String code){
        if(mp!=null&&mp.isPlaying()&&code.contains("stopAlert")){
            mp.stop();
        }else if(mp==null){
            mp = MediaPlayer.create(getApplicationContext(), R.raw.aleter);
            if(code.contains("loopAlert")){
                mp.setLooping(true);
            }
            AudioManager audio = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            int maxAudio = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxAudio , 0 );
            mp.start();
        }else if(mp!=null&&!mp.isLooping()){
            if(code.contains("loopAlert")){
                mp.setLooping(true);
            }
            mp.start();
        }
    }
    public void getPhoneState(){
        sharedPreferences = getSharedPreferences("phoneState", Context.MODE_PRIVATE);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sharedPreferences.edit().putString("imei",tm.getDeviceId()).commit();
        sharedPreferences.edit().putString("imsi",tm.getSimSerialNumber()).commit();
        sharedPreferences.edit().putString("phoneNum", tm.getLine1Number()).commit();
    }
    public static boolean isServiceRunning(Context mContext,String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    public void broadcast2BaiduService(String sender,boolean send,String content,Context context){
        Intent baiduServiceIntent =new Intent(BaiduMapService.INTERNAL_ACTION_BAIDU);
        baiduServiceIntent.putExtra("send", send);
        baiduServiceIntent.putExtra("sender", sender);
        baiduServiceIntent.putExtra("code", content);
        context.sendBroadcast(baiduServiceIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void lockScreen(){
        dpm.lockNow();
    }
}
