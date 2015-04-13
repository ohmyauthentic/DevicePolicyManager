package com.candy.getgpspoint;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by candy on 2015/3/28.
 */
public class AdminReceiver extends DeviceAdminReceiver {


    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        System.out.println("密码成功");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {

        super.onPasswordFailed(context, intent);
        System.out.println("密码错误");
    }

}
