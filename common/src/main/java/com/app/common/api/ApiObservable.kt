package com.app.common.api

import android.content.Context
import com.app.common.BuildConfig
import com.app.common.base.AppBaseActivity
import com.app.common.base.AppBaseApplication
import com.app.common.base.AppBaseFragment
import com.app.common.logger.Logger
import com.app.common.view.toastErrorNet
import com.app.common.view.toastInfo
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by wangru
 * Date: 2018/12/17  10:44
 * mail: 1902065822@qq.com
 * describe:
 */
fun <T> Observable<T>.subscribeExtApi(onNext: (result: T) -> Unit,
                                      onError: ((e: Throwable) -> Unit)? = null,
                                      onComplete: (() -> Unit)? = null,
                                      onSubscribe: ((disposable: Disposable) -> Unit)? = null,
                                      context: Context? = null,
                                      isShowLoad: Boolean = false,
                                      isCanCancel: Boolean = true,
                                      isToast: Boolean = true): Disposable {

    return LambdaObserver<T>(
            { result ->
                dismissLoad(context, isShowLoad)
                onNext(result)
            },
            { e ->
                dismissLoad(context, isShowLoad)
                errorToast(e, {
                    onError?.invoke(e) ?: run {
                        if (isToast) toastErrorNet(e)
                    }
                })
            },
            {
                dismissLoad(context, isShowLoad)
                onComplete?.invoke()
            },
            {
                addDisposable(context, it)
                showDialog(context, isShowLoad, isCanCancel, it)
                onSubscribe?.invoke(it)
            })
            .apply {
                subscribe(this)
            }
}

private fun errorToast(e: Throwable, block: () -> Unit) {
    if (e is ApiException) {
        block()
    } else if (e is ConnectException || e is UnknownHostException) {
        toastInfo("网络未连接")
    } else if (e is TimeoutException || e is SocketTimeoutException) {
        toastInfo("网络超时")
    } else if (e is JsonSyntaxException) {
        toastInfo("数据解析异常")
    } else {
        Logger.d("onError#${e.message}")
        if (BuildConfig.DEBUG) {
            toastInfo("未知异常${e.message}")
        } else {
            toastInfo("请求异常")
        }
    }
    e.printStackTrace()
}

private fun addDisposable(context: Context?, disposable: Disposable) {
    context?.let {
        when (it) {
            is AppBaseActivity -> it.addSubscription(disposable)
            is AppBaseFragment -> it.addSubscription(disposable)
        }
    }
}

private fun showDialog(context: Context?, isShowLoad: Boolean, isCanCancel: Boolean = true, disposable: Disposable) {
    context?.let {
        if (isShowLoad) {
            when (it) {
                is AppBaseActivity -> it.showLoadingDialog(isCanCancel)
                is AppBaseFragment -> it.showLoadingDialog(isCanCancel)
            }
        }
    }
}

private fun dismissLoad(context: Context?, isShowLoad: Boolean) {
    context?.let {
        if (isShowLoad) {
            when (it) {
                is AppBaseActivity -> it.dismissLoadingDialog()
                is AppBaseFragment -> it.dismissLoadingDialog()
            }
        }
    }
}
