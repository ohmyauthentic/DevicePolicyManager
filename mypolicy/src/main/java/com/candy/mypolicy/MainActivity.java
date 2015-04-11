package com.candy.mypolicy;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements  AdminReceiver.PasswordAttemp{
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdminReceiver.listenlists.add(this);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                //权限列表
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cpn);
                //描述(additional explanation)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");
//                startActivityForResult(intent, 0);
                startActivity(intent);
            }
        });
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
                Toast.makeText(getApplicationContext(),"锁屏",Toast.LENGTH_SHORT).show();
                if(dpm.isAdminActive(cpn)){
                    Toast.makeText(getApplicationContext(),"可以锁屏",Toast.LENGTH_SHORT).show();
                    dpm.lockNow();
                }
            }
        });
    }

    public void SetText(){
       if(dpm.isAdminActive(cpn)) {
            textView.setText("times"+dpm.getCurrentFailedPasswordAttempts());
        }
//        textView.setText("wrong");
    }

    @Override
    public void OnStateChanged(boolean sss) {
        SetText();
    }
}
