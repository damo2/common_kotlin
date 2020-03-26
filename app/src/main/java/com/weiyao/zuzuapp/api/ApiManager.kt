package com.weiyao.zuzuapp.api

import com.app.common.api.RequestApiManager
import com.app.common.by.NotNullSingle

/**
 * Created by wr
 * Date: 2019/4/23  20:06
 * mail: 1902065822@qq.com
 * describe:
 */
object ApiManager {
    var apiService by NotNullSingle<ApiService>()

    //application 初始化
    fun initApiService() {
        apiService = RequestApiManager.initRetrofit({ clientBuilder ->
            //添加拦截器
        }, { retrofitBuilder ->
        }, baseUrl = "http://www.test.com").create(ApiService::class.java)
    }
}