package com.app.common.utils

import android.content.Context
import android.content.pm.PackageManager
import com.app.common.logger.Logger

/**
 * Created by wr
 * Date: 2019/1/12  13:14
 * mail: 1902065822@qq.com
 * describe:
 */
object GradleUtils{
    fun getMetaDataString(context: Context, tag: String): String? {
        var appKey: String? = null
        try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appKey = appInfo.metaData.getString(tag)
            Logger.d("$tag=$appKey")
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.d("找不到$tag")
            e.printStackTrace()
        }
        return appKey
    }
    fun getMetaDataInt(context: Context, tag: String): Int? {
        var appKey: Int? = null
        try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appKey = appInfo.metaData.getInt(tag)
            Logger.d("$tag=$appKey")
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.d("找不到$tag")
            e.printStackTrace()
        }
        return appKey
    }
}
