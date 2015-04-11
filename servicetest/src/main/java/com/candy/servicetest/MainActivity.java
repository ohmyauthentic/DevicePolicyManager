package com.candy.servicetest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.candy.servicetest.communication.PostPosition;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends ActionBarActivity implements  AdminReceiver.PasswordAttemp{
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    private Intent serviceIntent = null;
    private PostPosition postPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent= new Intent(MainActivity.this,DevicePolicyManagerService.class);
        AdminReceiver.listenlists.add(this);
        postPosition = new PostPosition();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cpn = new ComponentName(this,AdminReceiver.class);
        active();

        findViewById(R.id.startbtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                  new PostAsyncTask().execute();
            }
        });
    }

    public void active(){
        if(dpm.isAdminActive(cpn)){}else{
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cpn);
        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");
//                startActivityForResult(intent, 0);
        startActivityForResult(intent,0);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(dpm.isAdminActive(cpn)){
            startService(serviceIntent);
        }
    }

    @Override
    public void OnStateChanged(boolean sss) {
    }
    class PostAsyncTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            postPosition.postData();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
