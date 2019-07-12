package com.damo.loginshared.sina

import android.app.Activity
import android.content.Intent
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeCommonBean
import com.damo.loginshared.Const
import com.damo.loginshared.api.ApiManager
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler


/**
 * Created by wr
 * Date: 2018/12/10  15:09
 * describe:
 */
class SinaLogin {
    private var mSsoHandler: SsoHandler? = null

    private var mCallback: ((isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit)? = null
    private var mInfoCallback: ((isSuc: Boolean, userBean: SinaUserBean?) -> Unit)? = null

    private var mAccessToken: Oauth2AccessToken? = null

    fun login(activity: Activity, callback: (isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit, userCallBack: ((isSuc: Boolean, userBean: SinaUserBean?) -> Unit)? = null) {
        //注册新浪微博 只注册一次
        WbSdk.install(activity, AuthInfo(activity, Const.SINA_APP_KEY, Const.SINA_REDIRECT_URL, Const.SINA_APP_SCOPE))
        mCallback = callback
        mInfoCallback = userCallBack
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
                        mInfoCallback?.invoke(true, it)
                    }, { e ->
                        mInfoCallback?.invoke(false, null)
                    })
        }

    }
}