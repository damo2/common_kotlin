package com.app.common.api.util

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
fun <T> composeCommon(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> composeLife(event: LifeCycleEvent, lifecycleSubject: PublishSubject<LifeCycleEvent>): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        val lifecycleObservable = lifecycleSubject.filter { activityLifeCycleEvent -> activityLifeCycleEvent == event }
        observable.takeUntil(lifecycleObservable).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
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
