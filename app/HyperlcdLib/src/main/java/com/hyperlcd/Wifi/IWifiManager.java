package com.hyperlcd.Wifi;

import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import androidx.core.app.ActivityCompat;

import com.hyperlcd.Utils.LogUtils;

import java.util.List;

/**
 * <p>HyperlcdLib WIFI控制器类</p>
 * </br>
 * <p>使用该类中的接口方法必须先调用 {@link IWifiManager#getInstance(Context,WifiManagerInterface)} (String)} 获取实例。</p>
 * 接口功能：
 * </br>
 * <ol>
 * <li>{@link IWifiManager#openWifi()} 打开Wifi</li>
 * <li>{@link IWifiManager#closeWifi()} 关闭Wifi</li>
 * </ol>
 */
public class IWifiManager {
    private Context mContext;
    private List<WifiConfiguration> mWifiConfigurations;
    private List<ScanResult> mWifiResultList = null;
    private WifiInfo mWifiInfo;
    private NetworkInfo.DetailedState mDetailedState;
    private NetworkInfo mNetworkInfo;
    private android.net.wifi.WifiManager.WifiLock mWifiLock;
    private android.net.wifi.WifiManager mWifiManager;
    private ConnectivityManager mConnectManager;
    private static IWifiManager mInstance = null;
    private WifiManagerInterface mWifiManagerInterface = null;

    public static final int WIFI_STATE_CONNECTING = 0x01;
    public static final int WIFI_STATE_AUTHENTICATING = 0x02;
    public static final int WIFI_STATE_OBTAINING_IPADDR = 0x03;
    public static final int WIFI_STATE_FAILED = 0x04;
    public static final int WIFI_STATE_CONNECTED_DHCP = 0x05;
    public static final int WIFI_STATE_CONNECTED_STATIC = 0x06;
    public static final int WIFI_STATE_DISCONNECTED = 0x07;

    /**
     * 获取wifi控制单例
     * @param context 上下文对象
     * @param wifiManagerInterface 用于接收wifi列表与wifi连接状态
     * @return WifiControl单例
     */
    public static IWifiManager getInstance(Context context, WifiManagerInterface wifiManagerInterface) {
        if (mInstance == null) {
            synchronized (IWifiManager.class) {
                if (mInstance == null) {
                    mInstance = new IWifiManager(context,wifiManagerInterface);
                }
            }
        }
        return mInstance;
    }

    public IWifiManager(Context context, WifiManagerInterface wifiManagerInterface){
        mContext = context;
        mWifiManager = (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, intentFilter);
        mWifiManagerInterface = wifiManagerInterface;
    }

    public interface WifiManagerInterface{
        void onWifiStateChange(int State);
        void onWifiSearchDone(List<ScanResult> wifiResultList);
    }

    /**
     * 查询wifi是否打开
     * @return
     */
    public boolean isWifiEnabled() {
        LogUtils.d("isWifiEnabled.");
        return this.mWifiManager.isWifiEnabled();
    }

    /**
     * 开始搜索wifi
     * @return
     */
    public boolean startScan() {
        LogUtils.d("start scan wifi.");
        return this.mWifiManager.startScan();
    }
    /**
     * 得到网络列表
     * @return List<ScanResult>
     */
    public List<ScanResult> getWifiList() {
        return mWifiManager.getScanResults();
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

    /**
     * 打开Wifi
     * @return 执行状态 true成功 false失败
     * **/

    public boolean openWifi() {
        LogUtils.d("open wifi");
        if(!mWifiManager.isWifiEnabled()){ //当前wifi不可用
            return mWifiManager.setWifiEnabled(true);
        }
        return false;
    }

    /**
     * 关闭Wifi
     * @return 执行状态 true成功 false失败
     * **/
    public boolean closeWifi() {
        LogUtils.d("close wifi");
        if(mWifiManager.isWifiEnabled()) {
           return mWifiManager.setWifiEnabled(false);
        }
        return false;
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


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(android.net.wifi.WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (mWifiResultList!=null){
                    mWifiResultList.clear();
                }

                // wifi已成功扫描到可用wifi。
                mWifiResultList = getWifiList();
                if (mWifiManagerInterface!=null){
                    mWifiManagerInterface.onWifiSearchDone(mWifiResultList);
                }
            }
            if (action.equals(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION)&&mWifiManagerInterface!=null) {
                mNetworkInfo = intent.getParcelableExtra(android.net.wifi.WifiManager.EXTRA_NETWORK_INFO);

                if (mNetworkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    //断开连接
                    mWifiManagerInterface.onWifiStateChange(WIFI_STATE_DISCONNECTED);
                } else if (mNetworkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    //连接成功
                    android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getSystemService(WIFI_SERVICE);
                    final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    DhcpInfo dhcpInfo=wifiManager.getDhcpInfo();
                    if(dhcpInfo.leaseDuration==0){
                        //静态IP配置方式
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_CONNECTED_STATIC);
                    }else{
                        //动态IP配置方式
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_CONNECTED_DHCP);
                    }

                }else {
                    mDetailedState = mNetworkInfo.getDetailedState();
                    if (mDetailedState == mDetailedState.CONNECTING) {
                        //连接中...
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_CONNECTING);
                    } else if (mDetailedState == mDetailedState.AUTHENTICATING) {
                        //正在验证身份信息...
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_AUTHENTICATING);
                    } else if (mDetailedState == mDetailedState.OBTAINING_IPADDR) {
                        //正在获取IP地址...
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_OBTAINING_IPADDR);
                    } else if (mDetailedState == mDetailedState.FAILED) {
                        //连接失败
                        mWifiManagerInterface.onWifiStateChange(WIFI_STATE_FAILED);
                    }

                }
            }

        }
    };

}
