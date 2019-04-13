package com.youke.yingba.base.api.upload

import io.reactivex.Observer
import okhttp3.ResponseBody

/**
 * Created by wangru
 * Date: 2018/7/6  16:12
 * mail: 1902065822@qq.com
 * describe:
 */

abstract class FileUpLoadObserver<T> : Observer<T> {

    override fun onNext(t: T) {
        onUpLoadSuccess(t)
    }

    override fun onError(e: Throwable) {
        onUpLoadFail(e)
    }

    //可以重写，具体可由子类实现
    override fun onComplete() {}

    //上传成功回调
    abstract fun onUpLoadSuccess(t: T)

    //上传失败回调
    abstract fun onUpLoadFail(throwable: Throwable)

    //上传进度监听
    abstract fun onProgress(up: Long, total: Long)

    fun dataToString(responseBody: ResponseBody) :String{
        return responseBody.string()
    }
}