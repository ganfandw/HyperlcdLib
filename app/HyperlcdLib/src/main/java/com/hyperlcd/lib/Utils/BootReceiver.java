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
            if (mSharedPreferences!=null){
                if (mSharedPreferences.getBoolean("POWER_BOOT",false)){
                    String packageName = mSharedPreferences.getString("PACKAGE_NAME","");
                    //如果自启动APP，参数为需要自动启动的应用包名
                    Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    //这句话必须加上才能开机自动运行app的界面
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //自启动Activity
                    context.startActivity(mIntent);
                }
                Log.e("POWER_BOOT","开机自启动"+mSharedPreferences.getBoolean("POWER_BOOT",false));
            }
        }
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
            Log.e("shutdown","关机了");
        }
    }
}