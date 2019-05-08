package com.app.common.view

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager

import com.app.common.R

/**
 * Created by wr
 * Date: 2019/5/8  16:56
 * mail: 1902065822@qq.com
 * describe:
 * 全局 Dialog 显示 需要浮动通知权限
 */
object DialogUtils {
    lateinit var dialogLoading: AlertDialog

    fun showLogoutDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        /*true 代表点击空白可消失   false代表点击空白哦不可消失 */
        builder.setCancelable(false)
        val view = View.inflate(context, R.layout.common_dialog_loading, null)
        builder.setView(view)
        dialogLoading = builder.create()

        //设置弹出全局对话框，但是这句话并不能解决在android的其他手机上能弹出来（例如用户华为p10 就无法弹框）
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

        //只有这样才能弹框
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogLoading.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            dialogLoading.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }


        dialogLoading.show()
    }


}
