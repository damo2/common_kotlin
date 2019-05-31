package com.app.common.update

import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * Created by wr
 * Date: 2019/5/29  19:56
 * mail: 1902065822@qq.com
 * describe:
 */
object UpdateApkUtil {
    fun updateInstallApk(context: Context, updateApkUrl: String) {
        val intent = Intent(context, UpdateService::class.java).apply { putExtra(ConstUpdate.KEY_DOWN_APK_URL, updateApkUrl) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}