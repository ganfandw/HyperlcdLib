package com.hyperlcd.lib.App;

import android.content.Context;
import android.content.SharedPreferences;

import com.hyperlcd.lib.WifiManager.WifiControl;

/**
 * <p>HyperlcdLib 开机启动控制器类</p>
 * </br>
 * <p>使用该类中的接口方法必须先调用 {@link WifiControl#getInstance(Context)} (String)} 获取实例。</p>
 * 接口功能：
 * </br>
 * <ol>
 * <li>{@link PowerBootControl#setPowerBootState(boolean)} true设置开机自启，false取消开机自启</li>
 * <li>{@link WifiControl#closeWifi()} 关闭Wifi</li>
 * </ol>
 */
public class PowerBootControl {

    private static PowerBootControl mInstance = null;
    private Context mContext;
    private String mPackageName;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    PowerBootControl(Context context){
        mContext = context;
        mEditor = mSharedPreferences.edit();
    }
    /**
     * 获取开机启动控制器类控制单例
     * @param context 上下文对象
     * @param packageName 需要启动应用的包名
     * @return PowerBootControl 单例
     */
    public static PowerBootControl getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PowerBootControl.class) {
                if (mInstance == null) {
                    mSharedPreferences = context.getSharedPreferences("hyperlcdLibShared",Context.MODE_PRIVATE);
                    mInstance = new PowerBootControl(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * <p>设置软件开机自启</p>
     * </br>
     * @Param packageName 需要自启动的应用包名
     * @return void
     */
    public void setPowerBootStart(String packageName){
        mEditor.putBoolean("POWER_BOOT",true);
        mEditor.putString("PACKAGE_NAME",packageName);
        mEditor.commit();
    }

    /**
     * <p>取消软件开机自启</p>
     * </br>
     * @return void
     */
    public void setPowerBootClose(){
        mEditor.putBoolean("POWER_BOOT",false);
        mEditor.commit();
    }

}
