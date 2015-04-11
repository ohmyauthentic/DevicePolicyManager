package com.candy.autotakepic;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity implements  AdminReceiver.PasswordAttemp{
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private TextView textView;
    private PowerManager.WakeLock wakeLock = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AdminReceiver.listenlists.add(this);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        textView = (TextView) findViewById(R.id.textView);
        active();
        findViewById(R.id.wipe).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dpm.isAdminActive(cpn)){
//                    dpm.wipeData(0);
                }
            }
        });
        findViewById(R.id.lock).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dpm.isAdminActive(cpn)){
                    dpm.lockNow();
                }
            }
        });
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
    public void SetText(){
       if(dpm.isAdminActive(cpn)) {
            textView.setText("解锁错了"+dpm.getCurrentFailedPasswordAttempts()+"次");
           Intent intent = new Intent();
           intent.setClass(MainActivity.this,TakePicture.class);
           startActivity(intent);
        }
//        textView.setText("wrong");
    }

    @Override
    public void OnStateChanged(boolean sss) {
        SetText();
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
}
