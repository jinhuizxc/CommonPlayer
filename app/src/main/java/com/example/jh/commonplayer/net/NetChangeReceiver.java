package com.example.jh.commonplayer.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jinhui on 2018/2/27.
 * Email:1004260403@qq.com
 */

public class NetChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        resetNetStatus(context);
        Log.e(TAG, "resetNetStatus"); // 不执行
    }

    // 判断网络状态
    public static void resetNetStatus(Context context) {
        Log.e(TAG, "resetNetStatus1");  // 执行
        int networkType = getNetworkType(context);
        switch (networkType) {
            case 1:
                Log.e(TAG, "networkType 1");  // 执行
                if (NetConstans.NET_STATUS != NetConstans.UN_CONNECTED) {
                    NetConstans.NET_STATUS = NetConstans.UN_CONNECTED;
                    EventBus.getDefault().post(new NetEvent(NetConstans.UN_CONNECTED));
                }
                break;
            case 2:
                Log.e(TAG, "networkType 2");  // 执行
            case 4:
                Log.e(TAG, "networkType 4");  // 执行
                if (NetConstans.NET_STATUS != NetConstans.CONNECTED_MOBILE) {
                    NetConstans.NET_STATUS = NetConstans.CONNECTED_MOBILE;
                    EventBus.getDefault().post(new NetEvent(NetConstans.CONNECTED_MOBILE));
                }
                break;
            case 3:
                Log.e(TAG, "networkType 3");  // 执行
                if (NetConstans.NET_STATUS != NetConstans.CONNECTED_WIFI) {
                    NetConstans.NET_STATUS = NetConstans.CONNECTED_WIFI;
                    EventBus.getDefault().post(new NetEvent(NetConstans.CONNECTED_WIFI));
                }
                break;
            default:
                Log.e(TAG, "networkType default");  // 执行
                if (NetConstans.NET_STATUS != NetConstans.NO_AVAILABLE) {
                    NetConstans.NET_STATUS = NetConstans.NO_AVAILABLE;
                    EventBus.getDefault().post(new NetEvent(NetConstans.NO_AVAILABLE));
                }
                break;

        }
    }

    /**
     * 判断当前网络类型
     * -1为未知网络
     * 0为没有网络连接
     * 1网络断开或关闭
     * 2为以太网
     * 3为WiFi
     * 4为2G
     * 5为3G
     * 6为4G
     */
    private static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            /** 没有任何网络 */
            return 0;
        }
        if (!networkInfo.isConnected()) {
            /** 网络断开或关闭 */
            return 1;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            /** 以太网网络 */
            return 2;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            /** wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接 */
            return 3;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            /** 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭 */
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    /** 2G网络 */
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    /** 3G网络 */
                case TelephonyManager.NETWORK_TYPE_LTE:
                    /** 4G网络 */
                    return 4;
            }
        }
        /** 未知网络 */
        return -1;
    }
}
