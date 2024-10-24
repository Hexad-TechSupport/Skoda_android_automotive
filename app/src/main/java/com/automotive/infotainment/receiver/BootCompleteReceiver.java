package com.automotive.infotainment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.automotive.infotainment.service.CarPropertyServiceManager;

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = BootCompleteReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"on Recieved called");
        if(context == null || intent == null){
            Log.i(TAG,"on recieve context or intent is null");
            return;
        }
        Intent vehicleAppServiceConnection = new Intent(context, CarPropertyServiceManager.class);
        context.startService(vehicleAppServiceConnection);

    }
}
