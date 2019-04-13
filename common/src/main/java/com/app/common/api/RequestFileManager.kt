package com.app.common.api

import com.app.common.BuildConfig
import com.app.common.CommonConst
import com.app.common.api.download.DownloadProgressInterceptor
import com.app.common.api.download.FileDownLoadObserver
import com.app.common.api.interceptor.LogInterceptor
import com.app.common.api.upload.FileRequestBody
import com.youke.yingba.base.api.upload.FileUpLoadObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by wangru
 * Date: 2018/7/6  16:26
 * mail: 1902065822@qq.com
 * describe:
 */

object RequestFileManager {

    //下载
    fun downloadFile(url: String, file: File,
                     fileDownLoadObserver: FileDownLoadObserver<File>, progressCallback: (totalLength: Long, contentLength: Long, done: Boolean) -> Unit) {
        getDownRetrofit(progressCallback)
                .create(CommonApiService::class.java)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { responseBody -> fileDownLoadObserver.saveFile(responseBody, file) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileDownLoadObserver)
    }

    //上传文件
    fun uploadFile(url: String, file: File, fileUpLoadObserver: FileUpLoadObserver<String>) {
        val requestFile = RequestBody.create(MultipartBody.FORM, file)
        val fileRequestBody = FileRequestBody(requestFile, fileUpLoadObserver)
        getUpRetrofit()
                .create(CommonApiService::class.java)
                .uploadFile(url, fileRequestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { fileUpLoadObserver.dataToString(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUpLoadObserver)
    }

    //上传文件from key
    fun uploadFileByKey(url: String, key: String, file: File, fileUpLoadObserver: FileUpLoadObserver<String>) {
        val requestFile = RequestBody.create(MultipartBody.FORM, file)
        val fileRequestBody = FileRequestBody(requestFile, fileUpLoadObserver)
        val multipartBody = MultipartBody.Part.createFormData(key, file.name, fileRequestBody)
        getUpRetrofit()
                .create(CommonApiService::class.java)
                .uploadFileByKey(url, multipartBody)
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { fileUpLoadObserver.dataToString(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUpLoadObserver)
    }

    private fun getDownRetrofit(progressCallback: (totalLength: Long, contentLength: Long, done: Boolean) -> Unit): Retrofit {
        val client = OkHttpClient.Builder()
                .connectTimeout(CommonConst.DOWNLOAD_OUTTIME, TimeUnit.MILLISECONDS)
                .addInterceptor(DownloadProgressInterceptor(progressCallback))
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(LogInterceptor())
                    }
                }
                .build()
        return Retrofit.Builder()
                .client(client)
                .baseUrl(CommonConst.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getUpRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
                .connectTimeout(CommonConst.UPLOAD_OUTTIME, TimeUnit.MILLISECONDS)
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(LogInterceptor())
                    }
                }
                .build()
        return Retrofit.Builder()
                .client(client)
                .baseUrl(CommonConst.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}