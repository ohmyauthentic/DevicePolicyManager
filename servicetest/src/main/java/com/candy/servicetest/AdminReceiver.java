package com.candy.servicetest;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by candy on 2015/3/28.
 */
public class AdminReceiver extends DeviceAdminReceiver {

   public static ArrayList<PasswordAttemp> listenlists = new ArrayList<>();

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
        System.out.println("xxxxxxxxxxxxxx");
        for(int i = 0;i<listenlists.size();i++){
            listenlists.get(i).OnStateChanged(false);
        }
    }

    public void addListener(PasswordAttemp listener){

        listenlists.add(listener);

    }

    public interface PasswordAttemp{
        public void OnStateChanged(boolean sss);
    }
}
