package com.hyperlcd.PowerBoot;

import android.content.Context;
import android.content.SharedPreferences;

import com.hyperlcd.Wifi.IWifiManager;

/**
 * <p>HyperlcdLib 开机启动控制器类</p>
 * </br>
 * <p>使用该类中的接口方法必须先调用 {@link IWifiManager#getInstance(Context)} (String)} 获取实例。</p>
 * 接口功能：
 * </br>
 * <ol>
 * <li>{@link IPowerBootManager#setPowerBootStart(String)}设置应用开机自启动</li>
 * <li>{@link IPowerBootManager#setPowerBootClose()} 关闭应用开机自启动</li>
 * </ol>
 */
public class IPowerBootManager {

    private static IPowerBootManager mInstance = null;
    private Context mContext;
    private String mPackageName;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    IPowerBootManager(Context context){
        mContext = context;
        mEditor = mSharedPreferences.edit();
    }
    /**
     * 获取开机启动控制器类控制单例
     * @param context 上下文对象
     * @return PowerBootControl 单例
     */
    public static IPowerBootManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (IPowerBootManager.class) {
                if (mInstance == null) {
                    mSharedPreferences = context.getSharedPreferences("hyperlcdLibShared",Context.MODE_PRIVATE);
                    mInstance = new IPowerBootManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置软件开机自启
     * @param packageName 需要自启动的应用包名
     * @return 无
     */
    public void setPowerBootStart(String packageName){
        mEditor.putBoolean("POWER_BOOT",true);
        mEditor.putString("PACKAGE_NAME",packageName);
        mEditor.commit();
    }

    /**
     * 取消软件开机自启
     * @return 无
     */
    public void setPowerBootClose(){
        mEditor.putBoolean("POWER_BOOT",false);
        mEditor.commit();
    }

}
