package com.candy.getgpspoint;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Intent serviceIntent = null;
    private PointReceiver receiver;
    private SharedPreferences sharedPreferences;
    public static final String INTENAL_ACTION_MAIN = "sun.phone.main";
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private PowerManager.WakeLock wakeLock = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(MainActivity.this, BaiduMapService.class);
        IntentFilter filter = new IntentFilter(INTENAL_ACTION_MAIN);
        startService(serviceIntent);
        receiver = new PointReceiver();
        registerReceiver(receiver,filter);
        getPhoneState();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        active();
    }

    public void active(){
        if(dpm.isAdminActive(cpn)) {}else{
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //权限列表
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cpn);
            //描述(additional explanation)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");
            startActivityForResult(intent, 0);
        }
    }
    public void getPhoneState(){
        sharedPreferences = getSharedPreferences("phoneState", Context.MODE_PRIVATE);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sharedPreferences.edit().putString("imei",tm.getDeviceId()).commit();
        sharedPreferences.edit().putString("imsi",tm.getSimSerialNumber()).commit();
        sharedPreferences.edit().putString("phoneNum", tm.getLine1Number()).commit();
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null)
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class PointReceiver extends BroadcastReceiver {

        //        public boolean mbRunFlagReceiver = false;
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String latitude = intent.getStringExtra("latitude");
//            ((TextView)findViewById(R.id.textView)).setText(sharedPreferences.getString("phoneNum","000"));
        }
    }

}
