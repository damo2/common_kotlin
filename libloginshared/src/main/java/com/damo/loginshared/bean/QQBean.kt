package com.youke.yingba.base.loginshare.bean
import com.google.gson.annotations.SerializedName



/**
 * Created by wr
 * Date: 2018/12/6  17:41
 * describe:
 */

data class QQDataBean(
    @SerializedName("ret") var ret: Int?,
    @SerializedName("openid") var openid: String?,
    @SerializedName("access_token") var accessToken: String?,
    @SerializedName("pay_token") var payToken: String?,
    @SerializedName("expires_in") var expiresIn: Int?,
    @SerializedName("pf") var pf: String?,
    @SerializedName("pfkey") var pfkey: String?,
    @SerializedName("msg") var msg: String?,
    @SerializedName("login_cost") var loginCost: Int?,
    @SerializedName("query_authority_cost") var queryAuthorityCost: Int?,
    @SerializedName("authority_cost") var authorityCost: Int?,
    @SerializedName("expires_time") var expiresTime: Long?
)

data class QQUserInfoBean(
    @SerializedName("ret") var ret: Int?,
    @SerializedName("msg") var msg: String?,
    @SerializedName("is_lost") var isLost: Int?,
    @SerializedName("nickname") var nickname: String?,
    @SerializedName("gender") var gender: String?,
    @SerializedName("province") var province: String?,
    @SerializedName("city") var city: String?,
    @SerializedName("year") var year: String?,
    @SerializedName("constellation") var constellation: String?,
    @SerializedName("figureurl") var figureurl: String?,
    @SerializedName("figureurl_1") var figureurl1: String?,
    @SerializedName("figureurl_2") var figureurl2: String?,
    @SerializedName("figureurl_qq_1") var figureurlQq1: String?,
    @SerializedName("figureurl_qq_2") var figureurlQq2: String?,
    @SerializedName("is_yellow_vip") var isYellowVip: String?,
    @SerializedName("vip") var vip: String?,
    @SerializedName("yellow_vip_level") var yellowVipLevel: String?,
    @SerializedName("level") var level: String?,
    @SerializedName("is_yellow_year_vip") var isYellowYearVip: String?
)