package com.app.common.extensions

import com.app.common.utils.ObserverKt
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by wangru
 * Date: 2018/12/17  10:03
 * mail: 1902065822@qq.com
 * describe:
 */
fun <T> Observable<T>.subscribeExt(onNext: (data: T) -> Unit, onError: ((e: Throwable) -> Unit)? = null,
                                   onComplete: (() -> Unit)? = null, onSubscribe: ((disposable: Disposable) -> Unit)? = null): Disposable =
        ObserverKt<T>(onNext, onError, onComplete, onSubscribe).apply { subscribe(this) }
