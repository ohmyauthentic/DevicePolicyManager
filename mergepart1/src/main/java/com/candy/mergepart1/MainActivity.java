package com.candy.mergepart1;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private Intent MainServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        active();
        startMainService();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mainServiceIntent =new Intent(MainService.INTERNAL_ACTION_MAIN);
                mainServiceIntent.putExtra("code", "Alert");
                getApplicationContext().sendBroadcast(mainServiceIntent);
//                Toast.makeText(getApplicationContext(),Tool.generateSequenceNo(),Toast.LENGTH_SHORT).show();
            }
        });        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mainServiceIntent =new Intent(MainService.INTERNAL_ACTION_MAIN);
                mainServiceIntent.putExtra("code", "loopAlert");
                getApplicationContext().sendBroadcast(mainServiceIntent);
//                Toast.makeText(getApplicationContext(),Tool.generateSequenceNo(),Toast.LENGTH_SHORT).show();
            }
        });        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mainServiceIntent =new Intent(MainService.INTERNAL_ACTION_MAIN);
                mainServiceIntent.putExtra("code", "stopAlert");
                getApplicationContext().sendBroadcast(mainServiceIntent);
//                Toast.makeText(getApplicationContext(),Tool.generateSequenceNo(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startMainService(){
        MainServiceIntent = new Intent(getApplicationContext(), MainService.class);
        MainServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(MainServiceIntent);
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

}
