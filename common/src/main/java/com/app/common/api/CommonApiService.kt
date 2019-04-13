package com.app.common.api

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 所有请求
 * Created by wangru
 * Date: 2018/7/6  15:02
 * mail: 1902065822@qq.com
 * describe:
 */
interface CommonApiService {

    @Streaming
    @GET
    fun downLoadFile(@NonNull @Url url: String): Observable<ResponseBody>

    @Multipart
    @POST("{url}")
    fun uploadFile(@Path(value = "url", encoded = true) url: String, @Part file: RequestBody): Observable<ResponseBody>

    @Multipart
    @POST("{url}")
    fun uploadFileByKey(@Path(value = "url", encoded = true) url: String, @Part file: MultipartBody.Part): Observable<ResponseBody>
}