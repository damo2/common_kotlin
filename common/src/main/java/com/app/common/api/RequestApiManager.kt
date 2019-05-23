package com.app.common.api

import com.app.common.BuildConfig
import com.app.common.CommonConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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

class RequestApiManager() {
    private var retrofit: Retrofit? = null
    private var client: OkHttpClient? = null

    private object SingletonHolder {
        val INSTANCE = RequestApiManager()
    }

    fun initRetrofit(clientBuilder: (builder: OkHttpClient.Builder) -> Unit = {}, retrofitBuilder: (builder: Retrofit.Builder) -> Unit = {}, baseUrl: String = CommonConst.BASE_URL) {
        if (client == null) {
            client = OkHttpClient.Builder().apply {
                connectTimeout(CommonConst.REQUEST_OUTTIME, TimeUnit.MILLISECONDS)
                addInterceptor(CommonInterceptor())
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                }
                clientBuilder(this);
            }.build()
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client!!)
                    .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .apply {
                        retrofitBuilder(this);
                    }
                    .build()
        }
    }

    //这里返回一个泛型类，主要返回的是定义的接口类
    fun <T> createService(clazz: Class<T>): T {
        if (retrofit == null) {
            initRetrofit();
        }
        return retrofit!!.create(clazz)
    }

    companion object {
        val instance: RequestApiManager
            get() = SingletonHolder.INSTANCE
    }

    private inner class CommonInterceptor(var isToken: Boolean = false, var isParams: Boolean = true) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url = originalHttpUrl.newBuilder().apply {
                if (isParams) {
                    //版本号
                    addQueryParameter("ver", "${BuildConfig.VERSION_CODE}")
                    addQueryParameter("vn", BuildConfig.VERSION_NAME)
                    //平台 1 android 2 ios
                    addQueryParameter("os", "1")
                }
            }.build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder().apply {
                //                if (isToken) {
//                    addHeader("accessToken", UserData.token)
//                }
            }.url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}
