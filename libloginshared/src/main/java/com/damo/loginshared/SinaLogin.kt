package com.damo.loginshared

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeCommonBean
import com.damo.loginshared.api.ApiManager
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.youke.yingba.base.loginshare.bean.SinaUserBean


/**
 * Created by wr
 * Date: 2018/12/10  15:09
 * describe:
 */
class SinaLogin(var activity: Activity) {
    private var mSsoHandler: SsoHandler? = null

    private var mCallback: ((isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit)? = null
    private var mCallbackUser: ((isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?, sinaUserBean: SinaUserBean?) -> Unit)? = null

    private var mAccessToken: Oauth2AccessToken? = null

    fun login(callback: (isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?) -> Unit) {
        mCallback = callback
        mSsoHandler = SsoHandler(activity)
        mSsoHandler?.authorize(SelfWbAuthListener())
    }

    fun login(callbackUser: (isSuc: Boolean, errorInfo: String?, accessToken: Oauth2AccessToken?, sinaUserBean: SinaUserBean?) -> Unit) {
        mCallbackUser = callbackUser
        mSsoHandler = SsoHandler(activity)
        mSsoHandler?.authorize(SelfWbAuthListener())
    }

    private inner class SelfWbAuthListener : com.sina.weibo.sdk.auth.WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken) {
            activity.runOnUiThread({
                mAccessToken = token
                if (mAccessToken?.isSessionValid == true) {
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken)
                    mCallbackUser?.let {
                        getUserInfo()
                    }
                    mCallback?.invoke(true, null, mAccessToken)
                } else {
                    mCallback?.invoke(false, "新浪微博授权失败", null)
                    mCallbackUser?.invoke(false, "新浪微博授权失败", null, null)
                }
            })
        }

        override fun cancel() {
            Toast.makeText(activity.applicationContext, "新浪微博取消授权", Toast.LENGTH_SHORT).show()
            mCallback?.invoke(false, "新浪微博取消授权", null)
            mCallbackUser?.invoke(false, "新浪微博取消授权", null, null)
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            Toast.makeText(activity.applicationContext, "新浪微博授权失败", Toast.LENGTH_SHORT).show()
            mCallback?.invoke(false, errorMessage.errorMessage, null)
            mCallbackUser?.invoke(false, errorMessage.errorMessage, null, null)
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
                        mCallbackUser?.invoke(true, null, accessToken, it)
                    }, { e ->
                        mCallbackUser?.invoke(true, e.message, accessToken, null)
                    }, context = activity, isShowLoad = true, isToast = true)
        }

    }
}