package com.app.common.utils

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.atomic.AtomicReference

class ObserverKt<T>(val onNextKt: (data: T) -> Unit, val onErrorKt: ((e: Throwable) -> Unit)? = null,
                    val onCompleteKt: (() -> Unit)? = null,
                    val onSubscribeKt: ((disposable: Disposable) -> Unit)? = null) : AtomicReference<Disposable>(), Observer<T>, Disposable {

    override fun onSubscribe(s: Disposable) {
        if (DisposableHelper.setOnce(this, s)) {
            try {
                onSubscribeKt?.invoke(this)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                s.dispose()
                onError(ex)
            }

        }
    }

    override fun onNext(t: T) {
        if (!isDisposed) {
            try {
                onNextKt(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                get().dispose()
                onError(e)
            }

        }
    }

    override fun onError(t: Throwable) {
        if (!isDisposed) {
            lazySet(DisposableHelper.DISPOSED)
            try {
                onErrorKt?.invoke(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }

        }
    }

    override fun onComplete() {
        if (!isDisposed) {
            lazySet(DisposableHelper.DISPOSED)
            try {
                onCompleteKt?.invoke()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }

        }
    }

    override fun dispose() {
        DisposableHelper.dispose(this)
    }

    override fun isDisposed(): Boolean {
        return get() === DisposableHelper.DISPOSED
    }

    companion object {
        private val serialVersionUID = -2118322957072273456L
    }
}
