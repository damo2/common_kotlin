package com.app.common.api

import com.app.common.BuildConfig
import com.app.common.CommonConst
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by wangru
 * Date: 2018/6/28  19:03
 * mail: 1902065822@qq.com
 * describe:
 */

object RequestApiManager {

    /**
     * 初始化retrofit
     * @param clientBuilder 设置okhttpClient
     * @param retrofitBuilder 设置retrofit
     * @param parameter 设置公用参数
     * @param header 设置公用head
     * @param baseUrl 项目请求地址 必须以“/”结尾
     * @param isShowHttpLog 是否显示日志 默认true 显示
     */
    fun initRetrofit(clientBuilder: ((builder: OkHttpClient.Builder) -> Unit)? = null, retrofitBuilder: ((builder: Retrofit.Builder) -> Unit)? = null, parameter: ((httpUrlBuilder: HttpUrl.Builder) -> Unit)? = null, header: ((requestBuilder: Request.Builder) -> Unit)? = null, baseUrl: String = CommonConst.BASE_URL, isShowHttpLog: Boolean = true): Retrofit {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(CommonConst.REQUEST_OUTTIME, TimeUnit.MILLISECONDS)
            addInterceptor(CommonInterceptor(parameter, header))
            if (isShowHttpLog && BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
            clientBuilder?.invoke(this);
        }.build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client!!)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .apply {
                    retrofitBuilder?.invoke(this);
                }
                .build()
    }

    //添加通用参数
    private class CommonInterceptor(var parameter: ((httpUrlBuilder: HttpUrl.Builder) -> Unit)?, var header: ((requestBuilder: Request.Builder) -> Unit)?) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("ver", "${BuildConfig.VERSION_CODE}")
                addQueryParameter("vn", BuildConfig.VERSION_NAME)
                //平台 1 android 2 ios
                addQueryParameter("os", "1")
                parameter?.invoke(this)
            }.build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder().apply {
                //                addHeader("token", UserData.token ?: "")
                header?.invoke(this)
            }.url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    //这里返回一个泛型类，主要返回的是定义的接口类
    fun <T> Retrofit.createService(clazz: Class<T>): T = this.create(clazz)

}
