package com.app.common.api.interceptor

import com.app.common.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by wr
 * Date: 2019/1/26  14:43
 * mail: 1902065822@qq.com
 * describe:
 * 重试拦截器
 * 有问题
 */

/**
 * @param maxRetry 最大重试次数
 */
class RetryTimeInterceptor(var maxRetry: Int = 5, var isDelay: Boolean = false) : Interceptor {
    //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
    private var retryNum = 0
    private var retryDelayTime: Long = 0//重试的间隔
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        while (!response.isSuccessful && retryNum < maxRetry) {
            if (isDelay) {
                retryDelayTime = if (retryDelayTime == 0L) 8000 else retryDelayTime * 3
                Thread.sleep(retryDelayTime)
                Logger.d("retryNum=$retryNum retryDelayTime=${retryDelayTime}")
            }
            retryNum++
            Logger.d("retryNum=$retryNum")
            response = chain.proceed(request)
        }
        return response
    }
}


