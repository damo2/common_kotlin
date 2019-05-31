package com.app.common.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.app.common.extensions.getAppIconDrawableExt
import com.app.common.extensions.getUriFromFileExt
import java.io.File


/**
 * Created by wangru
 * Date: 2018/7/24  14:51
 * mail: 1902065822@qq.com
 * describe:
 */
/**
 * 8.0以上系统设置安装未知来源权限
 */

object InstallAppCode {
    val INSTALL_PERMISSION_CODE = 101
}

class InstallApp(var apkFile: File) {
    fun installProcess(activity: Activity) {
        val haveInstallPermission: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = activity.packageManager.canRequestPackageInstalls()
            if (!haveInstallPermission) {
                //弹框提示用户手动打开
                showAlert(activity, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", DialogInterface.OnClickListener { dialog, which ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //此方法需要API>=26才能使用
                        toInstallPermissionSettingIntent(activity)
                    }
                })
                return
            }
        }
        installApk(activity)
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun toInstallPermissionSettingIntent(activity: Activity) {
        val packageURI = Uri.parse("package:" + activity.packageName)
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        activity.startActivityForResult(intent, InstallAppCode.INSTALL_PERMISSION_CODE)
    }

    /**
     * alert 消息提示框显示
     * @param context   上下文
     * @param title     标题
     * @param message   消息
     * @param listener  监听器
     */
    private fun showAlert(context: Context, title: String, message: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("确定", listener)
            setCancelable(false)
            setIcon(context.getAppIconDrawableExt())
        }.create().show()
    }

    //安装应用
    private fun installApk(activity: Activity) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
            } else {//Android7.0之后获取uri要用contentProvider
                val uri = activity.getUriFromFileExt(apkFile)
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(intent)
    }

}