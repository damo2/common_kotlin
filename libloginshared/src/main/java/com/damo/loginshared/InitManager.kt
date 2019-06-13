package com.damo.loginshared

import android.content.Context
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo

/**
 * Created by wr
 * Date: 2019/6/5  19:22
 * mail: 1902065822@qq.com
 * describe:
 */
object InitManager {
    fun init(context: Context){
        //注册微信分享
//        WechatShare.regWechat()
        //注册新浪微博
        WbSdk.install(context, AuthInfo(context, Const.SINA_APP_KEY, Const.SINA_REDIRECT_URL, Const.SINA_APP_SCOPE))
    }
}