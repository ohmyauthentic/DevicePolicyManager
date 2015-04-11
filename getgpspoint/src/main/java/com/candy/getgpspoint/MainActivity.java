package com.candy.getgpspoint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Intent serviceIntent = null;
    private PointReceiver receiver;
    private SharedPreferences sharedPreferences;
    public static final String INTENAL_ACTION_MAIN = "sun.phone.main";

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
/*        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BaiduMapService.INTENAL_ACTION_BAIDU);
                intent.putExtra("code","locate");
                sendBroadcast(intent);
            }
        });*/
    }

    public void getPhoneState(){
        sharedPreferences = getSharedPreferences("phoneState", Context.MODE_PRIVATE);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sharedPreferences.edit().putString("imei",tm.getDeviceId()).commit();
        sharedPreferences.edit().putString("imsi",tm.getSimSerialNumber()).commit();
        sharedPreferences.edit().putString("phoneNum",tm.getLine1Number()).commit();
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
            ((TextView)findViewById(R.id.textView)).setText(sharedPreferences.getString("phoneNum","000"));
        }
    }

}
