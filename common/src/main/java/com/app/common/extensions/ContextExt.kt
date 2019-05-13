package com.app.common.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.app.common.file.UpdateFile
import com.app.common.utils.AppInfoUtils
import com.app.common.utils.NetUtils
import java.io.File

//-------------------   appinfo -----------------
fun Context.getVersionCodeExt() = AppInfoUtils.getVersionCode(this)

fun Context.getVersionNameExt() = AppInfoUtils.getVersionName(this)

//获取ApplicationId
fun Context.getApplicationIdExt() = AppInfoUtils.getApplicationIdExt(this)

//是否运行在前台
fun Context.isAppRunningForegroundExt() = AppInfoUtils.isAppRunningForeground(this)

//设备唯一id
fun Context.getAndroidIDExt() = AppInfoUtils.getAndroidID(this)

//屏幕宽
fun Context.screenWidthExt() = AppInfoUtils.screenWidth(this)

//屏幕高
fun Context.screenHeightExt() = AppInfoUtils.screenHeight(this)


fun Context.getUriFromFileExt(file: File, applicationId: String = getApplicationIdExt()) = UpdateFile.getUriFromFile(this, file, applicationId)

//---------------------------     单位转换start        --------------------------------

fun Context.dp2px(dpVal: Int): Int = dp2px(dpVal.toFloat()).toInt()

fun Context.dp2pxFI(dpVal: Float): Int = dp2px(dpVal).toInt()
fun Context.dp2px(dpVal: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, this.resources.displayMetrics)

fun Context.sp2px(spVal: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, this.resources.displayMetrics)


//---------------------------     获取资源start        --------------------------------

fun Context.getDrawableExt(@DrawableRes id: Int, @Nullable theme: Resources.Theme?) = ResourcesCompat.getDrawable(resources, id, theme)

fun Context.getDrawableExt(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.getColorExt(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getColorExt(@ColorRes id: Int, theme: Resources.Theme?) =
        ResourcesCompat.getColor(this.resources, id, theme)

//getColorStateList过时方法处理
fun Context.getColorStateListExt(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)

//getColorStateList过时方法处理
fun Context.getColorStateListExt(@ColorRes id: Int, theme: Resources.Theme?) = ResourcesCompat.getColorStateList(this.resources, id, theme)

//----------------------------   netutil ----------------------------------

//打开网络设置界面
fun Context.openWirelessSettingsExt() = NetUtils.openWirelessSettings(this)

/**
 * 获取活动网络信息
 */
fun Context.getActiveNetworkInfoExt() = NetUtils.getActiveNetworkInfo(this)

/**
 * 判断网络是否可用
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 */
fun Context.isAvailableExt() = NetUtils.isAvailable(this)

/**
 * 判断网络是否连接
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 */
fun Context.isConnectedExt() = NetUtils.isConnected(this)

/**
 * 判断网络是否是4G
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 */
fun Context.is4GExt(): Boolean = NetUtils.is4G(this)

/**
 * 判断wifi是否连接状态
 * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
 */
fun Context.isWifiConnectedExt() = NetUtils.isWifiConnected(this)

/**
 * 获取移动网络运营商名称
 * @return 移动网络运营商名称  如中国联通、中国移动、中国电信
 */
fun Context.getNetworkOperatorNameExt() = NetUtils.getNetworkOperatorName(this)

fun Context.getPhoneTypeExt() = NetUtils.getPhoneType(this)

fun Context.getNetworkTypeExt() = NetUtils.getNetworkType(this)
/**
 * 获取当前的网络类型(WIFI,2G,3G,4G)
 *  NETWORK_WIFI、 NETWORK_4G、NETWORK_3G、NETWORK_2G、NETWORK_UNKNOWN、NETWORK_NO
 */
fun Context.getNetWorkTypeNameExt() = NetUtils.getNetWorkTypeName(this)