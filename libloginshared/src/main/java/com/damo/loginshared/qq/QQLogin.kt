package com.damo.loginshared.qq

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.damo.loginshared.Const
import com.google.gson.Gson
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


/**
 * Created by wr
 * Date: 2018/12/6  17:19
 * describe:
 */
class QQLogin() {
    private var mCallbackToken: ((isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit)? = null
    private var mInfoCallback: ((isSuc: Boolean, qqUserInfoBean: QQUserInfoBean?, errorInfo: String?) -> Unit)? = null

    private var mTencent: Tencent? = null
    private var mLoginQQListener: BaseUiListener? = null


    fun login(activity: Activity, callback: (isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit,
              infoCallback: ((isSuc: Boolean, qqUserInfoBean: QQUserInfoBean?, errorInfo: String?) -> Unit)? = null) {
        this.mCallbackToken = callback
        this.mInfoCallback = infoCallback

        mTencent = Tencent.createInstance(Const.QQ_APP_ID, activity)
        mLoginQQListener = BaseUiListener(activity)
        mTencent?.login(activity, "all", mLoginQQListener)
    }


    private fun getQQUserInfo(activity: Activity, qqDataBean: QQDataBean) {
        mTencent?.setAccessToken(qqDataBean.accessToken, qqDataBean.expiresIn.toString())
        mTencent?.openId = qqDataBean.openid

        UserInfo(activity, mTencent?.qqToken).getUserInfo(object : IUiListener {
            override fun onComplete(data: Any?) {
                val qqUserBean = Gson().fromJson(data?.toString(), QQUserInfoBean::class.java)
                Log.d("QQLogin", "onComplete#获取信息")
                mInfoCallback?.invoke(true, qqUserBean, "")
            }

            override fun onCancel() {
                mInfoCallback?.invoke(false, null, "取消")
            }

            override fun onError(e: UiError?) {
                mInfoCallback?.invoke(false, null, e?.errorMessage ?: "异常")
            }

        })
    }


    private inner class BaseUiListener(var activity: Activity) : IUiListener {
        override fun onComplete(data: Any?) {
            val qqDataBean = Gson().fromJson(data?.toString(), QQDataBean::class.java)
            mInfoCallback?.let {
                getQQUserInfo(activity, qqDataBean)
            }
            mCallbackToken?.invoke(true, qqDataBean)
        }

        override fun onError(e: UiError?) {
            mCallbackToken?.invoke(false, null)
            Log.e("QQLoginShare", "onError#${e?.errorMessage}")
        }

        override fun onCancel() {
            mCallbackToken?.invoke(false, null)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginQQListener)
        }
    }

}