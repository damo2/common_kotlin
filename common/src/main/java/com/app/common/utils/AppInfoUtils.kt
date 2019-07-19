package com.app.common.utils

import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import com.app.common.CommonConst
import com.app.common.logger.Logger
import com.app.common.logger.logd
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by wr
 * Date: 2019/5/10  14:45
 * mail: 1902065822@qq.com
 * describe:
 */
object AppInfoUtils {
    fun getVersionCode(context: Context): Int? =
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: Exception) {
                null
            }


    fun getVersionName(context: Context): String? =
            try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }

    /**
     * 获取applicationId
     *
     * 注意:需要在app模块 AndroidManifest.xml 里面添加
     *
     * <meta-data
     *  android:name="APPLICATION_ID"
     *  android:value="${applicationId}"/>
     */
    fun applicationIdExt(context: Context): String =
            MetaDataUtil.getMetaDataString(context, CommonConst.APPLICATION_ID)

    fun getAppName(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0);
            val labelRes = packageInfo.applicationInfo.labelRes
            context.resources.getString(labelRes);
        } catch (e: Exception) {
            null
        }
    }

    fun isNamedProcess(processName: String): Boolean {
        val pid = android.os.Process.myPid()
        return getProcessName(pid) == processName
    }

    fun getProcessName(pid: Int = android.os.Process.myPid()): String? {
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
    fun isAppRunningForeground(context: Context): Boolean {
        val var1 = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return try {
            val taskInfoList = var1.getRunningTasks(1)
            if (taskInfoList != null && taskInfoList.size >= 1) {
                context.packageName.equals((taskInfoList[0] as ActivityManager.RunningTaskInfo).baseActivity.packageName, ignoreCase = true)
            } else {
                false
            }
        } catch (e: SecurityException) {
            Logger.d("isAppRunningForeground#Apk doesn't hold GET_TASKS permission")
            e.printStackTrace()
            false
        }

    }

    //设备唯一id
    fun getAndroidID(context: Context): String {
        val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        var id = androidID + Build.SERIAL
        try {
            id = toMD5(id)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return id
    }

    //屏幕宽
    fun screenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    //屏幕高
    fun screenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }


    /** 通过[context]和[packageName]获取App图标 */
    fun getAppIcon(context: Context, packageName: String = context.packageName): Drawable? {
        if (packageName.isBlank()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 检查设备是否有虚拟键盘
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs
                .getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass,
                    "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
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

    /**
     * 当前进程是否是运行serviceClass的进程
     */
    fun isInServiceProcess(context: Context, serviceClass: Class<out Service>): Boolean {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, PackageManager.GET_SERVICES)
        } catch (e: Exception) {
            logd("Could not get package info for ${context.packageName}")
            return false
        }

        val mainProcess = packageInfo.applicationInfo.processName

        val component = ComponentName(context, serviceClass)
        val serviceInfo: ServiceInfo
        try {
            serviceInfo = packageManager.getServiceInfo(component, 0)
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Service is disabled.
            return false
        }

        if (serviceInfo.processName == mainProcess) {
            logd("Did not expect service $serviceClass to run in main process $mainProcess")
            // Technically we are in the service process, but we're not in the service dedicated process.
            return false
        }

        val myPid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var myProcess: ActivityManager.RunningAppProcessInfo? = null
        val runningProcesses: List<ActivityManager.RunningAppProcessInfo>?
        try {
            runningProcesses = activityManager.runningAppProcesses
        } catch (exception: SecurityException) {
            // https://github.com/square/leakcanary/issues/948
            logd("Could not get running app processes $exception")
            return false
        }

        if (runningProcesses != null) {
            for (process in runningProcesses) {
                if (process.pid == myPid) {
                    myProcess = process
                    break
                }
            }
        }
        if (myProcess == null) {
            logd("Could not find running process for $myPid")
            return false
        }

        return myProcess.processName == serviceInfo.processName
    }
}