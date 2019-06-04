package com.damo.loginshared.api

import com.youke.yingba.base.loginshare.bean.SinaUserBean
import com.youke.yingba.base.loginshare.bean.WxInfoBean
import com.youke.yingba.base.loginshare.bean.WxTokenBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by wr
 * Date: 2019/6/4  14:24
 * mail: 1902065822@qq.com
 * describe:
 */
interface ApiService {
    //------------   第三方登录  ------------
    //获取微信token
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    fun getWeChatToken(@Query("appid") appid: String, @Query("secret") secret: String,
                       @Query("code") code: String, @Query("grant_type") grant_type: String = "authorization_code"): Observable<WxTokenBean>

    //获取微信信息
    @GET("https://api.weixin.qq.com/sns/userinfo")
    fun getWeChatInfo(@Query("access_token") access_token: String, @Query("openid") openid: String): Observable<WxInfoBean>

    //获取
    @GET("https://api.weibo.com/2/users/show.json")
    fun getSinaInfo(@Query("access_token") access_token: String, @Query("uid") uid: String): Observable<SinaUserBean>
}