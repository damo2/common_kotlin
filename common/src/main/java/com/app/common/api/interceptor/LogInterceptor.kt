package com.app.common.api.interceptor


import android.util.Log
import okhttp3.FormBody
import java.net.URLDecoder

/**
 * 日志拦截
 * Created by wangru
 * mail: 1902065822@qq.com
 * describe:
 */

class LogInterceptor : BaseInterceptor(requestCallback = {
    try {
        val method = it.method();

        val urlStr = buildString {
            if ("POST" == method) {
                if (it.body() is FormBody) {
                    val body = it.body() as FormBody
                    for (i in 0..(body.size() - 1)) {
                        append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                    }
                    delete(length - 1, length);
                }
            }
        }

        //URLDecoder.decode URLDecode解码
        val info = "Request{" +
                "method=[${it.method()}]" +
                ", url=[${URLDecoder.decode(it.url().toString())}]\n" +
                ", headers=[" + it.headers().toString() + "]" +
                ", isHttps=" + it.isHttps +
                ", Params=[${URLDecoder.decode(urlStr, "utf-8")}]" +
                '}'
        Log.i("LogInterceptor", "intercept#request:\n$info")
    } catch (e: Exception) {
    }
}, resultCallback = { result, _ ->
    Log.i("LogInterceptor", "intercept#result:\n${result}")
})
