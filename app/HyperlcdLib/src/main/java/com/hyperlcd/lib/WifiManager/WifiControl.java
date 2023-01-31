package com.hyperlcd.lib.WifiManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.hyperlcd.lib.Utils.LogUtils;

import java.util.List;

/**
 * @author zhou
 * @description:Wifi功能控制类
 * @date : 2023/1/31 13:44
 */
public class WifiControl {

    private Context mContext;
    private List<WifiConfiguration> mWifiConfigurations;
    private List<ScanResult> mWifiResultList;
    private WifiInfo mWifiInfo;
    private WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;
    private ConnectivityManager mConnectManager;
    private static WifiControl mInstance = null;

    /**
     * 获取wifi控制单例
     * @param context 上下文对象
     * @return WifiControl单例
     */
    public static WifiControl getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WifiControl.class) {
                if (mInstance == null) {
                    mInstance = new WifiControl(context);
                }
            }
        }
        return mInstance;
    }

    private WifiControl(Context context){
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mConnectManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取wifi当前状态
     * @return
     *  WIFI_STATE_DISABLING（0）：正在关闭
     *  WIFI_STATE_DISABLED（1）：已经关闭
     *  WIFI_STATE_ENABLING（2）：正在打开
     *  WIFI_STATE_ENABLED（3）：已经打开
     *  WIFI_STATE_UNKNOWN（4）：未知状态
     */
    public int getWifiState()
    {
        LogUtils.d("---getWifiState---");
        return mWifiManager.getWifiState();
    }

    /**
     * 获取wifi是否打开
     * @return true为打开状态 false为关闭状态
     */
    public boolean isEnabled()
    {
        boolean enable = mWifiManager.isWifiEnabled();
        LogUtils.d("wifi is enable:" + enable);
        return enable;
    }

    /**打开Wifi**/
    public void OpenWifi() {
        LogUtils.d("open wifi");
        if(!this.mWifiManager.isWifiEnabled()){ //当前wifi不可用
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    /**关闭Wifi**/
    public void closeWifi() {
        LogUtils.d("close wifi");
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }


}
