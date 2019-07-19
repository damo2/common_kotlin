package com.app.common.update

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.app.common.bean.DownApkEvent
import com.app.common.extensions.appNameExt
import com.app.common.rxbus2.RxBus
import com.app.common.utils.SingleHolder
import com.app.common.utils.StorageUtils
import com.app.common.view.toastInfo
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Created by wr
 * Date: 2019/5/29  19:56
 * mail: 1902065822@qq.com
 * describe:
 */
class UpdateInstallApk {
    private var apkFile: File? = null

    companion object : SingleHolder<UpdateInstallApk>(::UpdateInstallApk)

    /**
     * 在Activity onActivityResult里面添加  UpdateApkUtil.onActivityResult(requestCode, resultCode, activity) 处理授权安装app
     */
    @RequiresPermission(allOf = [(Manifest.permission.READ_EXTERNAL_STORAGE), (Manifest.permission.WRITE_EXTERNAL_STORAGE)])
    fun updateInstallApk(activity: Activity, downApkUrl: String, installAppPath: String? = null, listener: ((disposable: Disposable) -> Unit)? = null): Disposable? {
        //默认下载路径
        val defaultInstallPath = StorageUtils.getPublicStorageDir(activity.appNameExt
                ?: "apk") + File.separator + downApkUrl.substring(downApkUrl.lastIndexOf("/"), downApkUrl.length)
        val intent = Intent(activity, UpdateService::class.java).apply {
            putExtra(ConstUpdate.KEY_DOWN_APK_URL, downApkUrl)
            putExtra(ConstUpdate.KEY_INSTALL_APK_PATH, installAppPath ?: defaultInstallPath)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent)
        } else {
            activity.startService(intent)
        }

        return RxBus.toFlowable().subscribe { event ->
            if (event is DownApkEvent) {
                if (event.isSuc) {
                    apkFile = event.file
                    apkFile?.let { InstallApp(it).installProcess(activity) }
                } else {
                    toastInfo("下载失败，请重试")
                }
            }
        }.apply {
            listener?.invoke(this)
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, activity: Activity) {
        if (resultCode == Activity.RESULT_OK && requestCode == InstallAppCode.INSTALL_PERMISSION_CODE) {
            //请求安装权限成功，重新安装
            apkFile?.let { InstallApp(it).installProcess(activity) }
        }
    }
}