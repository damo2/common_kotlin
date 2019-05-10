package com.app.common.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.telephony.TelephonyManager

/**
 * Created by wr
 * Date: 2019/5/10  14:55
 * mail: 1902065822@qq.com
 * describe:
 */
object NetUtils {
    enum class NetworkType(val type: Int) {
        NETWORK_NO(-1),
        NETWORK_WIFI(1),
        NETWORK_2G(2),
        NETWORK_3G(3),
        NETWORK_4G(4),
        NETWORK_UNKNOWN(5)
    }

    /**
     * 打开网络设置界面
     */
    fun openWirelessSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    }

    /**
     * 获取活动网络信息
     */
    fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 判断网络是否可用
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    fun isAvailable(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info?.isAvailable ?: false
    }

    /**
     * 判断网络是否连接
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info?.isConnected ?: false
    }

    /**
     * 判断网络是否是4G
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable && info.subtype == TelephonyManager.NETWORK_TYPE_LTE
    }

    /**
     * 判断wifi是否连接状态
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     */
    fun isWifiConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 获取移动网络运营商名称
     *
     * 如中国联通、中国移动、中国电信
     * @return 移动网络运营商名称
     */
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }

    /**
     * 获取移动终端类型
     * @return 手机制式
     *
     *  * [TelephonyManager.PHONE_TYPE_NONE] : 0 手机制式未知
     *  * [TelephonyManager.PHONE_TYPE_GSM] : 1 手机制式为GSM，移动和联通
     *  * [TelephonyManager.PHONE_TYPE_CDMA] : 2 手机制式为CDMA，电信
     *  * [TelephonyManager.PHONE_TYPE_SIP] : 3
     *
     */
    fun getPhoneType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     */
    fun getNetworkType(context: Context): NetworkType {
        val info = getActiveNetworkInfo(context)
        return info?.let {
            when (it.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkType.NETWORK_WIFI
                ConnectivityManager.TYPE_MOBILE -> when (info.subtype) {

                    TelephonyManager.NETWORK_TYPE_GSM,
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G

                    TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_3G

                    TelephonyManager.NETWORK_TYPE_IWLAN,
                    TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                || subtypeName.equals("WCDMA", ignoreCase = true)
                                || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_UNKNOWN
                        }
                    }
                }
                else -> NetworkType.NETWORK_UNKNOWN
            }
        } ?: NetworkType.NETWORK_NO
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     *  * NETWORK_WIFI
     *  * NETWORK_4G
     *  * NETWORK_3G
     *  * NETWORK_2G
     *  * NETWORK_UNKNOWN
     *  * NETWORK_NO
     *
     */
    fun getNetWorkTypeName(context: Context): String {
        return getNetworkType(context).name
    }
}