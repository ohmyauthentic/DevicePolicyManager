package com.candy.mergepart1;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by candy on 2015/3/28.
 */
public class AdminReceiver extends DeviceAdminReceiver {
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Toast.makeText(context,"解锁了",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        System.out.println("密码成功xxxxxx");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
/*        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int s = dpm.getCurrentFailedPasswordAttempts();
        Log.d("xxxxxx", "attemps"+s);
        dpm=null;*/
        Intent mainServiceIntent =new Intent(MainService.INTERNAL_ACTION_MAIN);
        mainServiceIntent.putExtra("code", "passwordFailed");
        context.sendBroadcast(mainServiceIntent);
    }

}
