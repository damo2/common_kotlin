package com.damo.loginshared.wechat

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.annotation.IntDef
import com.damo.loginshared.Const
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by wr
 * Date: 2018/8/28  21:17
 * describe:
 * @see {@link https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN}
 */

object WechatShare {


    const val WX_FRIEND = 0
    const val WX_CIRCLE = 1

    //注解
//    @IntDef(WX_FRIEND, WX_CIRCLE)
//    @Retention(AnnotationRetention.SOURCE)
//    @Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
//    annotation class WechatShareType

    class WechatShareType {
        @IntDef(WX_FRIEND, WX_CIRCLE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class MyState

        companion object {
            const val WX_FRIEND = 0
            const val WX_CIRCLE = 1
        }
    }

    /**
     * @param flag        0 微信好友 1 微信朋友圈
     * @param url         点击跳转链接
     * @param title       分享的标题
     * @param description 分享的具体内容
     * @param thumb       分享时显示的图片（R.drawable.icon_logo）  不能超过32k
     */
    fun shareToWechat(context: Context,@WechatShareType.MyState flag: Int, url: String, title: String, description: String, thumb: Bitmap?) {
        val api: IWXAPI = WXAPIFactory.createWXAPI(context, Const.WX_APPID, true).apply { registerApp(Const.WX_APPID) }
        var content = description
        if (flag != WechatShareType.WX_CIRCLE && flag != WechatShareType.WX_FRIEND) {
            return
        }

        if (!api.isWXAppInstalled) {
            Toast.makeText(context, "未安装微信客户端", Toast.LENGTH_SHORT).show()
            return
        }

        val msg = WXMediaMessage(
                WXWebpageObject().apply {
                    webpageUrl = url
                })
        msg.title = title
        if (content.length > 32) {
            content = content.substring(0, 30) + "..."
        }
        msg.description = content
        // 这里替换一张自己工程里的图片资源
        msg.setThumbImage(thumb)
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg
        req.scene = if (flag == 0) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
        api.sendReq(req)
    }
}
