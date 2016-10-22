package com.anyonavinfo.bluetoothphone.bpservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpservice.service.BluetoothPhoneService;

/**
 * Created by Drive on 2016/10/17.
 */




    public class StartReceiver extends BroadcastReceiver {

        static final String ACTIONBoot = "android.intent.action.BOOT_COMPLETED";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTIONBoot)) {
                context.startService(new Intent(context, BluetoothPhoneService.class));
                Log.e("BluetoothPhone", "Broadcast " + ACTIONBoot + " Received.");
            }
        }

}
