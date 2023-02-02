package com.hyperlcd.lib.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static SharedPreferences mSharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot

            mSharedPreferences = context.getSharedPreferences("hyperlcdLibShared",Context.MODE_PRIVATE);
            Log.e("power boot","开机自启动"+mSharedPreferences.getBoolean("POWER_BOOT",false));
//            Intent intent2 = new Intent(context, LoginActivity.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent2);
        }
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
            Log.e("shutdown","关机了");
        }
    }
}