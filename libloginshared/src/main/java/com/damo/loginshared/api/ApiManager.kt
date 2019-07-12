package com.damo.loginshared.api

import com.app.common.api.RequestApiManager

/**
 * Created by wr
 * Date: 2019/4/23  20:06
 * mail: 1902065822@qq.com
 * describe:
 */
object ApiManager {
    val apiService: ApiService by lazy {
        RequestApiManager.instance.apply {
            initRetrofit({ clientBuilder ->
                //添加拦截器
            }, { retrofitBuilder ->
            }, "http://www.test.com")
        }.createService(ApiService::class.java)
    }
}