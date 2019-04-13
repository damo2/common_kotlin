package com.app.common.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by wr
 * Date: 2019/2/19  16:09
 * mail: 1902065822@qq.com
 * describe:
 */
object MetaDataUtil {
    /**
     * 获取 manifestPlaceholders 的String类型的值
     * 注意当String全是数字会被当成int类型，数字大取不出来
     */
    fun getMetaDataString(context: Context, key: String): String {
        var appKey: String? = null
        try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appKey = appInfo.metaData.getString(key)
            if (appKey == null) {
                appKey = appInfo.metaData.getInt(key).toString()
            }
            if (appKey == "0") {
                appKey = appInfo.metaData.getLong(key).toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appKey ?: ""
    }
}