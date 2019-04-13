package com.app.common.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.app.common.BuildConfig
import com.app.common.R
import com.app.common.api.ApiException
import com.google.gson.JsonSyntaxException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


class ToastX constructor(val mContext: Context) : Toast(mContext) {
    private var mToastX: ToastX? = null

    fun info(messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        info(mContext.getString(messageId))
    }

    fun info(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        custom(message)
    }

    fun success(messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        error(mContext.getString(messageId))
    }

    fun success(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        custom(message)
    }

    fun error(messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        error(mContext.getString(messageId))
    }

    fun error(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        custom(message)
    }

    fun errorNet(e: Throwable) {
        var msg = e.message
//        if (!BuildConfig.DEBUG) {
//        msg = "网络请求出错"
//        }
        custom(msg)
    }

    fun netError(e: Throwable, icon: Int? = null, duration: Int = Toast.LENGTH_SHORT) {
        val message = when (e) {
        //处理服务器返回的错误
            is ApiException -> e.message
            is ConnectException, is UnknownHostException -> "网络异常"
            is TimeoutException, is SocketTimeoutException -> "网络超时"
            is JsonSyntaxException -> "数据解析异常"
            else -> if (BuildConfig.DEBUG) "未知异常${e.message}" else "请求异常"
        }
        custom(message)
    }

    private fun custom(message: String?, duration: Int = Toast.LENGTH_SHORT) {
//        custom2(message)
        Handler(Looper.getMainLooper()).post {
            cancelToastX()
            val mLayout = View.inflate(mContext, R.layout.view_toast, null)
            val mToastLayout = mLayout.findViewById<View>(R.id.toast_root)
            mLayout.findViewById<TextView>(R.id.toast_text).setText(message)
            mToastX = ToastX(mContext).apply {
                setView(mLayout)
                setGravity(Gravity.CENTER_HORIZONTAL, 0, 1)
                setDuration(duration)
            }
//            showAnimator(mToastLayout)
            mToastX?.show()
        }
    }

    private fun showAnimator(toastLayout: View) {
        AnimatorSet().apply {
            playTogether(
                    ObjectAnimator.ofFloat(toastLayout, "translationY", 100f, 0f),
                    ObjectAnimator.ofFloat(toastLayout, "alpha", 0.6f, 1f)
            )
            duration = 200L
        }.start()
    }

    fun cancelToastX() {
        mToastX?.cancel()
    }

    override fun cancel() {
        try {
            super.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}