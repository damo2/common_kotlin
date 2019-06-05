package com.damo.loginshared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.youke.yingba.base.loginshare.bean.QQDataBean
import com.youke.yingba.base.loginshare.bean.QQUserInfoBean


/**
 * Created by wr
 * Date: 2018/12/6  17:19
 * describe:
 */
class QQLogin(var context: Context) {
    private var mCallbackToken: ((isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit)? = null
    private var mCallback: ((isSuc: Boolean, qqDataBean: QQDataBean?, qqUserInfoBean: QQUserInfoBean?) -> Unit)? = null

    private val mTencent by lazy { Tencent.createInstance(Const.QQ_APP_ID, context) }
    private val mLoginQQListener = BaseUiListener()
    private var mIsServerSideLogin = false
    private var mInfo: UserInfo? = null

    fun login(activity: Activity, callback: (isSuc: Boolean, qqDataBean: QQDataBean?, qqUserInfoBean: QQUserInfoBean?) -> Unit) {
        this.mCallback = callback
        login(activity)
    }

    fun login(activity: Activity, callback: (isSuc: Boolean, qqDataBean: QQDataBean?) -> Unit) {
        this.mCallbackToken = callback
        login(activity)
    }

    private fun login(activity: Activity) {
        mTencent.login(activity, "all", mLoginQQListener)
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

        mInfo = UserInfo(context, mTencent.qqToken)
        mInfo?.getUserInfo(object : IUiListener {
            override fun onComplete(data: Any?) {
                val qqUserBean = Gson().fromJson(data?.toString(), QQUserInfoBean::class.java)
                Log.d("QQLoginShare", "onComplete#获取信息")
                mCallback?.invoke(true, qqDataBean, qqUserBean)
            }

            override fun onCancel() {
                mCallback?.invoke(false, qqDataBean, null)
            }

            override fun onError(e: UiError?) {
                mCallback?.invoke(false, qqDataBean, null)
            }

        })
    }


    private inner class BaseUiListener : IUiListener {
        override fun onComplete(data: Any?) {
            val qqDataBean = Gson().fromJson(data?.toString(), QQDataBean::class.java)
            mCallback?.let {
                getQQUserInfo(qqDataBean)
            }
            mCallbackToken?.invoke(true, qqDataBean)
        }

        override fun onError(e: UiError?) {
            mCallback?.invoke(false, null, null)
            mCallbackToken?.invoke(false, null)
            Log.e("QQLoginShare", "onError#${e?.errorMessage}")
            Toast.makeText(context, "QQ登录异常", Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            mCallback?.invoke(false, null, null)
            mCallbackToken?.invoke(false, null)
            Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginQQListener)
        }
    }

}