package com.app.common.api.transformer

import com.app.common.api.util.LifeCycleEvent
import com.orhanobut.hawk.Hawk
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by wr
 * Date: 2019/1/28  9:22
 * mail: 1902065822@qq.com
 * describe:
 *
 */

fun <T> Observable<T>.composeDefault(): Observable<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

//根据生命周期取消订阅，默认结束时取消
fun <T> Observable<T>.composeLife(lifecycleSubject: PublishSubject<LifeCycleEvent>, event: LifeCycleEvent = LifeCycleEvent.DESTROY): Observable<T> =
        takeUntil(
                lifecycleSubject.filter { activityLifeCycleEvent ->
                    activityLifeCycleEvent == event
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()
                )

fun <T> composeCommon(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.composeDefault()
    }
}

fun <T> composeLife(lifecycleSubject: PublishSubject<LifeCycleEvent>, event: LifeCycleEvent = LifeCycleEvent.DESTROY): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.composeLife(lifecycleSubject, event)
    }
}

/**
 * @param cacheKey 缓存key
 * @param isSave 是否缓存数据 默认不缓存
 * @param forceRefresh 是否强制刷新 默认强制刷新
 */
fun <T> composeCache(isSave: Boolean = false, cacheKey: String, isBoth: Boolean = true, forceRefresh: Boolean = false): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        val fromCache = Observable.create(ObservableOnSubscribe<T> { e ->
            val cache = Hawk.get<Any>(cacheKey) as T
            if (cache != null) {
                e.onNext(cache)
            } else {
                e.onComplete()
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        var fromNetwork = observable
        if (isSave) {
            //这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
            fromNetwork = observable.map { t ->
                Hawk.put(cacheKey, t)
                t
            }
        }
        //强制刷新
        when {
            isBoth -> Observable.merge(fromCache, fromNetwork)
            forceRefresh -> fromNetwork
            else -> Observable.concat(fromCache, fromNetwork).firstElement().toObservable()
        }
    }
}
