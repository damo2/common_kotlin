package com.damo.loginshared

import android.app.Activity
import android.content.Intent
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeCommonBean
import com.damo.loginshared.api.ApiManager
import com.damo.loginshared.bean.SinaUserBean
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler


/**
 * Created by wr
 * Date: 2018/12/10  15:09
 * describe:
 */
class SinaLogin {
    private var mSsoHandler: SsoHandler? = null

    private var mCallback: ((isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit)? = null
    private var mInfoCallback: ((isSuc: Boolean, userBean: SinaUserBean?, errorInfo: String?) -> Unit)? = null

    private var mAccessToken: Oauth2AccessToken? = null

    fun login(activity: Activity, callback: (isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit, userCallBack: ((isSuc: Boolean, userBean: SinaUserBean?) -> Unit)? = null) {
        mCallback = callback
        mSsoHandler = SsoHandler(activity)
        mSsoHandler?.authorize(SelfWbAuthListener(activity))
    }

    private inner class SelfWbAuthListener(var activity: Activity) : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken) {
            activity.runOnUiThread {
                mAccessToken = token
                if (mAccessToken?.isSessionValid == true) {
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken)
                    mInfoCallback?.let {
                        getUserInfo()
                    }
                    mCallback?.invoke(true, null, mAccessToken)
                } else {
                    mCallback?.invoke(false, "新浪微博授权失败", null)
                }
            }
        }

        override fun cancel() {
            mCallback?.invoke(false, "新浪微博取消授权", null)
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            mCallback?.invoke(false, errorMessage.errorMessage, null)
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }

    private fun getUserInfo() {
        mAccessToken?.let { accessToken ->
            ApiManager.apiService
                    .getSinaInfo(accessToken.token, accessToken.uid)
                    .compose(composeCommonBean())
                    .subscribeExtApi({
                        mInfoCallback?.invoke(true, it, "")
                    }, { e ->
                        mInfoCallback?.invoke(false, null, "获取用户信息失败")
                    })
        }

    }
}