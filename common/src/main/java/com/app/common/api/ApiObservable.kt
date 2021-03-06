package com.app.common.api

import android.content.Context
import com.app.common.BuildConfig
import com.app.common.base.AppBaseActivity
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
 * @param onNext 成功
 * @param onError 失败 不传默认toast ApiException的异常信息，可以传onError 能toast自己的信息 例如：{toastInfo("添加失败")}
 * @param onComplete 完成
 * @param context 继承AppBaseActivity（或AppBaseFragment）的传this 用于统一取消监听， isShowLoad为true 必传
 * @param isShowLoad 是否显示加载框
 * @param isCanCancel 是否能被取消
 * @param isToast 是否toast失败信息
 */
fun <T> Observable<T>.subscribeExtApi(onNext: (result: T) -> Unit,
                                      onError: ((e: Throwable) -> Unit)? = null,
                                      onComplete: (() -> Unit)? = null,
                                      onSubscribe: ((disposable: Disposable) -> Unit)? = null,
                                      context: Any? = null,
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
                errorToast(e) {
                    onError?.invoke(e) ?: run {
                        if (isToast) toastErrorNet(e)
                    }
                }
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

private fun addDisposable(context: Any?, disposable: Disposable) {
    context?.let {
        when (it) {
            is AppBaseActivity -> it.addSubscription(disposable)
            is AppBaseFragment -> it.addSubscription(disposable)
        }
    }
}

private fun showDialog(context: Any?, isShowLoad: Boolean, isCanCancel: Boolean = true, disposable: Disposable) {
    context?.let {
        if (isShowLoad) {
            when (it) {
                is AppBaseActivity -> if (!it.isFinishing) it.showLoadingDialog(isCanCancel)
                is AppBaseFragment -> if (!it.isVisible) it.showLoadingDialog(isCanCancel)
            }
        }
    }
}

private fun dismissLoad(context: Any?, isShowLoad: Boolean) {
    if (isShowLoad) {
        context?.let {
            when (it) {
                is AppBaseActivity -> it.dismissLoadingDialog()
                is AppBaseFragment -> it.dismissLoadingDialog()
            }
        }
    }
}
