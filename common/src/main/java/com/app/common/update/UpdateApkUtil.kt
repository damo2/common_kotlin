package com.app.common.update

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.app.common.extensions.getAppNameExt
import com.app.common.utils.StorageUtils
import java.io.File

/**
 * Created by wr
 * Date: 2019/5/29  19:56
 * mail: 1902065822@qq.com
 * describe:
 */
object UpdateApkUtil {
    @RequiresPermission(allOf = [(Manifest.permission.READ_EXTERNAL_STORAGE), (Manifest.permission.WRITE_EXTERNAL_STORAGE)])
    fun updateInstallApk(context: Context, downApkUrl: String, installAppPath: String? = null) {
        //默认下载路径
        val defaultInstallPath = StorageUtils.getPublicStorageDir(context.getAppNameExt()
                ?: "apk") + File.separator + downApkUrl.substring(downApkUrl.lastIndexOf("/"), downApkUrl.length)
        val intent = Intent(context, UpdateService::class.java).apply {
            putExtra(ConstUpdate.KEY_DOWN_APK_URL, downApkUrl)
            putExtra(ConstUpdate.KEY_INSTALL_APK_PATH, installAppPath ?: defaultInstallPath)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}