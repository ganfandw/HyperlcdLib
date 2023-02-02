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
 * <li>{@link WifiControl#openWifi()} 打开Wifi</li>
 * <li>{@link WifiControl#closeWifi()} 关闭Wifi</li>
 * </ol>
 */
public class PowerBootControl {

    private static PowerBootControl mInstance = null;
    private Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    PowerBootControl(Context context){
        mContext = context;
        mEditor = mSharedPreferences.edit();
    }
    /**
     * 获取开机启动控制器类控制单例
     * @param context 上下文对象
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
     * <p>设置软件开机自启状态</p>
     * </br>
     * @Param flag boolean型变量 true为开机自启，false为取消开机自启
     * @return void
     */
    public void setPowerBootState(boolean flag){
        mEditor.putBoolean("POWER_BOOT",flag);
        mEditor.commit();
    }
}
