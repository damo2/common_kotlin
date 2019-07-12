package com.damo.loginshared.wechat

import android.content.Context
import com.app.common.view.toastInfo
import com.damo.loginshared.Const
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by wr
 * Date: 2019/7/11  19:16
 * mail: 1902065822@qq.com
 * describe:
 */
class WechatLogin(var context: Context) {

    private val api: IWXAPI = WXAPIFactory.createWXAPI(context, Const.WX_APPID).apply { registerApp(Const.WX_APPID) }

    fun login() {
        if (!api.isWXAppInstalled) {
            // 提醒用户没有安装微信
            toastInfo("未检测到微信客户端")
            return
        }
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "diandi_wx_login"

        api.sendReq(req)
    }
}