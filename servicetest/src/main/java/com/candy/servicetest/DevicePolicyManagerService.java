package com.candy.servicetest;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class DevicePolicyManagerService extends Service implements AdminReceiver.PasswordAttemp {
    private DevicePolicyManager dpm;
    private ComponentName cpn;
    public DevicePolicyManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AdminReceiver.listenlists.add(this);
    }

    @Override
    public void OnStateChanged(boolean sss) {
        Intent intent = new Intent(getBaseContext(),Transparent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
