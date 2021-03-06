package com.weiyao.zuzuapp.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * 所有请求
 * Created by wangru
 * Date: 2018/7/6  15:02
 * mail: 1902065822@qq.com
 * describe:
 */
interface ApiService {
    //------------   测试------------
    @GET("http://www.zuzuapp.com:18080/zuzu/version/checkForceVersion")
    fun update(@Query("user_version") version: String): Observable<JsonObject>


}