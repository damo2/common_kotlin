package com.damo.loginshared.sina

import android.app.Activity
import android.graphics.Bitmap
import com.damo.loginshared.Const
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.share.WbShareHandler


/**
 * Created by wr
 * Date: 2018/8/28  21:17
 * describe: 微博分享工具类
 */

object SinaShare {

    //activity 实现WbShareCallback接口回调
    fun shareToWeibo(activity: Activity, title: String, description: String, bitmap: Bitmap?) {
        //注册新浪微博 只注册一次
        WbSdk.install(activity.applicationContext, AuthInfo(activity.applicationContext, Const.SINA_APP_KEY, Const.SINA_REDIRECT_URL, Const.SINA_APP_SCOPE))

        val mWbShareHandler = WbShareHandler(activity)
        mWbShareHandler.registerApp()

        //分享内容
        val weiboMultiMessage = WeiboMultiMessage()
        //文本
        val textObject = TextObject()
        textObject.text = title + description
        weiboMultiMessage.textObject = textObject
        //图片
        val imageObject = ImageObject()
        imageObject.setImageObject(bitmap)
        weiboMultiMessage.imageObject = imageObject
        mWbShareHandler.shareMessage(weiboMultiMessage, false)
    }
}