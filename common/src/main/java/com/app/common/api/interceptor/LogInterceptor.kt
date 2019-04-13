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
        val sb = StringBuilder();
        if ("POST" == method) {
            if (it.body() is FormBody) {
                val body = it.body() as FormBody
                for (i in 0..(body.size() - 1)) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                }
                sb.delete(sb.length - 1, sb.length);
            }
        }
        //URLDecoder.decode URLDecode解码
        val info = "Request{" +
                "method=[${it.method()}]" +
                ", url=[${URLDecoder.decode(it.url().toString())}]\n" +
                ", headers=[" + it.headers().toString() + "]" +
                ", isHttps=" + it.isHttps +
                ", Params=[${URLDecoder.decode(sb.toString(), "utf-8")}]" +
                '}'
        Log.i("LogInterceptor", "intercept#request:\n$info")
    } catch (e: Exception) {
    }
}, resultCallback = {result, request ->
    Log.i("LogInterceptor", "intercept#result:\n${result}")
})
