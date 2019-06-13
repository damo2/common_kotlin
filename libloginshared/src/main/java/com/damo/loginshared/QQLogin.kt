package com.damo.loginshared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.common.utils.SingleHolder
import com.app.common.utils.SingleHolder1
import com.damo.loginshared.bean.QQDataBean
import com.damo.loginshared.bean.QQUserInfoBean
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
class QQLogin (var activity: Activity) {
    private var mCallbackToken: ((isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit)? = null
    private var mInfoCallback: ((isSuc: Boolean, qqUserInfoBean: QQUserInfoBean?, errorInfo: String?) -> Unit)? = null

    private lateinit var mTencent: Tencent
    private lateinit var mLoginQQListener: BaseUiListener

    fun login(callback: (isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit,
              infoCallback: ((isSuc: Boolean, qqUserInfoBean: QQUserInfoBean?, errorInfo: String?) -> Unit)? = null) {

        this.mCallbackToken = callback
        this.mInfoCallback = infoCallback
        login()
    }

    private fun login() {
        mLoginQQListener = BaseUiListener()
        mTencent = Tencent.createInstance(Const.QQ_APP_ID, activity)
        if (!mTencent.isSessionValid) {
            mTencent.login(activity, "all", mLoginQQListener)
        }

        //        if (!mTencent.isSessionValid()) {
//            mTencent.login(mActivity, "all", mLoginQQListener)
//            mIsServerSideLogin = false
//        } else {
//            if (mIsServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
//                mTencent.logout(mActivity)
//                mTencent.login(mActivity, "all", mLoginQQListener)
//                mIsServerSideLogin = false
//                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime())
//                return
//            }
//            mTencent.logout(mActivity)
//        }
    }

    private fun getQQUserInfo(qqDataBean: QQDataBean) {
        mTencent.setAccessToken(qqDataBean.accessToken, qqDataBean.expiresIn.toString())
        mTencent.openId = qqDataBean.openid

        UserInfo(activity, mTencent.qqToken).getUserInfo(object : IUiListener {
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


    private inner class BaseUiListener() : IUiListener {
        override fun onComplete(data: Any?) {
            val qqDataBean = Gson().fromJson(data?.toString(), QQDataBean::class.java)
            mInfoCallback?.let {
                getQQUserInfo(qqDataBean)
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