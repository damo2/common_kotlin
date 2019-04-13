package com.damo.libdb.cache

import android.graphics.Bitmap
import android.util.LruCache
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.UnsupportedEncodingException

/**
 * Created by wr
 * Date: 2019/1/24  13:51
 * mail: 1902065822@qq.com
 * describe:内存缓存
 */
object MemoryCache : ICache {
    private var mCache: LruCache<String, Any>? = null
        get() {
            if (field == null) {
                field = getCache()
            }
            return field
        }

    private fun getCache(): LruCache<String, Any> {
        //给LruCache分配1/32 24M
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        val mCacheSize = maxMemory / 16
        return object : LruCache<String, Any>(mCacheSize) {
            override fun sizeOf(key: String, value: Any): Int {
                return when (value) {
                    is Bitmap -> value.byteCount
                    else -> {
                        try {
                            value.toString().toByteArray(charset("UTF-8")).size ?: 0
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                            value.toString().toByteArray().size ?: 0
                        }
                    }
                }
            }
        }
    }

    //保存
    private fun getSaveCacheCommonObservable(cacheKey: String?, data: Any?): Observable<Boolean> =
            Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                if (cacheKey != null && data != null) {
                    mCache?.put(cacheKey, data)
                }
                emitter.onNext(true)
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    private fun <I> getQueryCacheCommonObservable(cacheKey: String?): Observable<I> =
            Observable.create(ObservableOnSubscribe<I> { emitter ->
                cacheKey?.let {
                    (mCache?.get(it) as? I)?.let {
                        emitter.onNext(it)
                    }
                }
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    private fun getDeleteCacheCommonObservable(cacheKey: String?): Observable<Boolean> =
            Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                cacheKey?.let {
                    mCache?.remove(it)
                }
                emitter.onNext(true)
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun put(key: String?, data: Any?): Observable<Boolean> =
            getSaveCacheCommonObservable(key, data)

    override fun getString(key: String?): Observable<String> =
            getQueryCacheCommonObservable(key)

    override fun <T> getBean(key: String?, type: Class<T>): Observable<T?> =
            getQueryCacheCommonObservable(key)

    override fun <I> getBeanList(key: String?, type: Class<I>): Observable<List<I>?> =
            getQueryCacheCommonObservable(key)

    override fun delete(key: String?): Observable<Boolean> =
            getDeleteCacheCommonObservable(key)

    //---------------------  同步  ---------------
    override fun putSync(key: String?, data: Any?): Boolean {
        key?.let {
            mCache?.put(it, data)
            true
        }
        return false
    }

    override fun getStringSync(key: String?): String? = mCache?.get(key) as? String

    override fun <I> getBeanSync(key: String?, type: Class<I>): I? = mCache?.get(key) as? I

    override fun <I> getBeanListSync(key: String?, type: Class<I>): List<I>? = mCache?.get(key) as? List<I>

    override fun deleteSync(key: String?): Boolean = mCache?.remove(key ?: "") != null ?: false
}
