package com.app.common.api.download

import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

/**
 * Created by wr
 * Date: 2018/11/1  10:56
 * describe:
 */

class DownloadProgressInterceptor(private var progressCallback: (totalLength: Long, contentLength: Long, done: Boolean)->Unit) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val originalResponse = chain.proceed(chain.request())
        originalResponse.body()?.let {
            return originalResponse.newBuilder()
                    .body(DownloadProgressResponseBody(it, progressCallback))
                    .build()
        }
        return null
    }
}