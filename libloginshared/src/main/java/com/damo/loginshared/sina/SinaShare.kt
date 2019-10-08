package com.damo.loginshared.sina

import android.app.Activity
import android.graphics.Bitmap
import com.damo.loginshared.Const
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.share.WbShareHandler
import com.sina.weibo.sdk.utils.Utility


/**
 * Created by wr
 * Date: 2018/8/28  21:17
 * describe: 微博分享工具类
 */

object SinaShare {

    //activity 实现WbShareCallback接口回调
    fun shareToWeibo(mActivity: Activity, url: String, title: String, description: String, bitmap: Bitmap?) {
        //注册微博sdk
        val mAuthInfo = AuthInfo(mActivity, Const.SINA_APP_KEY, Const.SINA_REDIRECT_URL, Const.SINA_APP_SCOPE)
        WbSdk.install(mActivity, mAuthInfo)
        val mWbShareHandler = WbShareHandler(mActivity)
        mWbShareHandler.registerApp()

        //分享内容
        val weiboMultiMessage = WeiboMultiMessage()

        val textObject = TextObject()
        textObject.text = title + description
        weiboMultiMessage.textObject = textObject

        val mediaObject = WebpageObject()
        mediaObject.identify = Utility.generateGUID()
        mediaObject.title = title
        mediaObject.description = description
//        val bitmap = BitmapFactory.decodeResource(mActivity.resources, R.drawable.icon_yingba)
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap)
        mediaObject.actionUrl = url
        mediaObject.defaultText = description
        weiboMultiMessage.mediaObject = mediaObject
        mWbShareHandler.shareMessage(weiboMultiMessage, false)
    }
}