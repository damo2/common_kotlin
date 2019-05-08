package com.app.common.api.upload

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

/**
 * Created by wangru
 * Date: 2018/7/6  16:12
 * mail: 1902065822@qq.com
 * describe:
 */

class FileUpLoadObserver<T>(val upSuccessCallback: ((t: T) -> Unit)?, val upFailCallback: ((e: Throwable) -> Unit)?, var upProgressCallback: ((progress: Long?, total: Long?) -> Unit)?) : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        upSuccessCallback?.invoke(t)
    }

    override fun onError(e: Throwable) {
        upFailCallback?.invoke(e)
    }

    fun dataToString(responseBody: ResponseBody): String = responseBody.string()
}