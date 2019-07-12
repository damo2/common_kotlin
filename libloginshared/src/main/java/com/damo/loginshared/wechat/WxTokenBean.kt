package com.damo.loginshared.wechat

/**
 * Created by zq on 2018/9/18
 */
data class WxTokenBean(var access_token: String, val expires_in: String, val refresh_token: String,
                       val openid: String, val scope: String, val unionid: String)

data class WxInfoBean(var sex: Int, var openid: String, var nickname: String, var province: String, var city: String,
                      var country: String, var headimgurl: String, var unionid: String, var privilege: List<Any>)

data class WxBean(var code: String = "")