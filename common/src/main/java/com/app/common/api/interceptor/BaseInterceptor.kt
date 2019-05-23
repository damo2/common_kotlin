package com.app.common.api.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset

/**
 * 拦截
 * Created by wangru
 * Date: 2017/9/20  9:37
 * mail: 1902065822@qq.com
 * @param requestCallback request 请求的数据
 * @param resultCallback result 返回的json数据
 * describe:param
 */
open class BaseInterceptor(var requestCallback: ((request: Request) -> Unit)? = null,
                           var resultCallback: ((result: String, request: Request) -> Unit)? = null) : Interceptor {
    companion object {
        val TAG = javaClass.simpleName
        private val UTF8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        requestCallback?.invoke(request)
        val response = chain.proceed(request)
        response?.body()?.let {
            val source = it.source()
            val contentType = it.contentType()
            val contentLength = it.contentLength()
            if (!bodyEncoded(response.headers())) {
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                val charset: Charset = try {
                    contentType?.charset(UTF8) ?: UTF8
                } catch (e: Exception) {
                    UTF8
                }
                if (!isPlaintext(buffer)) {
                    return response
                }
                if (contentLength != 0L) {
                    val result = buffer.clone().readString(charset)
                    resultCallback?.invoke(result, request)
                }
            }
        }
        return response
    }

    private fun bodyEncoded(headers: Headers) =
            headers.get("Content-Encoding")?.equals("identity", ignoreCase = true)?.not() ?: false

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }
}
