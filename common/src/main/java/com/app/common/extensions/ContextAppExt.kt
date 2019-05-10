package com.app.common.extensions

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import com.app.common.CommonConst
import com.app.common.logger.Logger
import com.app.common.utils.MetaDataUtil
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


fun Context.getVersionCode(): Int? =
        try {
            val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
            packageInfo.versionCode
        } catch (e: Exception) {
            null
        }


fun Context.getVersionName(): String? =
        try {
            val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }


fun Context.getApplicationIdExt(): String =
        MetaDataUtil.getMetaDataString(this, CommonConst.APPLICATION_ID)


fun Context.isNamedProcess(processName: String): Boolean {
    val pid = android.os.Process.myPid()
    return getProcessName(pid) == processName
}

//fun Context.getProcessName(pid: Int = android.os.Process.myPid()): String? {
//    return BufferedReader(FileReader("/proc/$pid/cmdline")).use {
//        it.readLine().trim()
//    }
//}

fun Context.getProcessName(pid: Int = android.os.Process.myPid()): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            if (reader != null) {
                reader.close()
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
    return null
}

//是否运行在前台
fun Context.isAppRunningForeground(): Boolean {
    val var1 = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    try {
        val taskInfoList = var1.getRunningTasks(1)
        if (taskInfoList != null && taskInfoList.size >= 1) {
            return this.packageName.equals((taskInfoList[0] as ActivityManager.RunningTaskInfo).baseActivity.packageName, ignoreCase = true)
        } else {
            return false
        }
    } catch (e: SecurityException) {
        Logger.d("isAppRunningForeground#Apk doesn't hold GET_TASKS permission")
        e.printStackTrace()
        return false
    }

}


//设备唯一id
fun Context.getAndroidID(): String {
    val androidID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    var id = androidID + Build.SERIAL
    try {
        id = toMD5(id)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return id
}

private fun toMD5(text: String): String {
    //获取摘要器 MessageDigest
    val messageDigest = MessageDigest.getInstance("MD5")
    //通过摘要器对字符串的二进制字节数组进行hash计算
    val digest = messageDigest.digest(text.toByteArray())

    return buildString {
        for (i in digest.indices) {
            //循环每个字符 将计算结果转化为正整数;
            val digestInt = digest[i].toInt() and 0xff
            //将10进制转化为较短的16进制
            val hexString = Integer.toHexString(digestInt)
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length < 2) {
                append(0)
            }
            //将循环结果添加到缓冲区
            append(hexString)
        }
    }
}

//屏幕宽
fun Context.screenWidthExt(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}

//屏幕高
fun Context.screenHeightExt(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}
