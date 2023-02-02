package com.hyperlcd.lib.WifiManager;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.core.app.ActivityCompat;

import com.hyperlcd.lib.Utils.LogUtils;

import java.util.List;

/**
 * <p>HyperlcdLib WIFI控制器类</p>
 * </br>
 * <p>使用该类中的接口方法必须先调用 {@link WifiControl#getInstance(Context)} (String)} 获取实例。</p>
 * 接口功能：
 * </br>
 * <ol>
 * <li>{@link WifiControl#openWifi()} 打开Wifi</li>
 * <li>{@link WifiControl#closeWifi()} 关闭Wifi</li>
 * </ol>
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
     * @Description: [获取wifi当前状态]
     * @Param: 无
     * @Return: ${WIFI_STATE_DISABLING（0）：正在关闭
     * WIFI_STATE_DISABLED（1）：已经关闭
     * WIFI_STATE_ENABLING（2）：正在打开
     * WIFI_STATE_ENABLED（3）：已经打开
     * WIFI_STATE_UNKNOWN（4）：未知状态
     * }
     * @CreateDate: ${date} ${time}</p>
     * @update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */

    public int getWifiState()
    {
        LogUtils.d("---getWifiState---");
        return mWifiManager.getWifiState();
    }

    /**
     * 获取wifi是否打开
     * @Param null
     * @return true为打开状态 false为关闭状态
     */
    public boolean isEnabled()
    {
        boolean enable = mWifiManager.isWifiEnabled();
        LogUtils.d("wifi is enable:" + enable);
        return enable;
    }

    /**打开Wifi**/
    public void openWifi() {
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

    /**
     * 无密码连接
     * @param ssid
     */
    public boolean connectWifiNoPwd(String ssid){
        mWifiManager.disableNetwork(mWifiManager.getConnectionInfo().getNetworkId());
        mWifiManager.removeNetwork(mWifiManager.getConnectionInfo().getNetworkId());
        int netId = mWifiManager.addNetwork(getWifiConfig(ssid, "", false));
        LogUtils.d("---connectWifiNoPws---netId:" + netId);
        return mWifiManager.enableNetwork(netId, true);
    }
    /**
     * wifi设置
     * @param ssid
     * @param pwd
     * @param isHasPws
     */
    private WifiConfiguration getWifiConfig(String ssid, String pwd, boolean isHasPws){
        LogUtils.d("--getWifiConfig----");
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        WifiConfiguration tempConfig = isExist(ssid);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (isHasPws){
            config.preSharedKey = "\""+pwd+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /**
     * 得到配置好的网络连接
     * @param ssid
     * @return
     */
    public WifiConfiguration isExist(String ssid) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        if(0 == configs.size())
        {
            LogUtils.d("no save wifi configuration.");
            return null;
        }
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }


}
