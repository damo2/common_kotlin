package com.damo.libdb.cache

import io.reactivex.Observable

/**
 * Created by wr
 * Date: 2019/1/24  15:55
 * mail: 1902065822@qq.com
 * describe:
 * 缓存管理
 */
object CacheManager {

    fun put(key: String?, data: Any?) =
            Observable.merge(MemoryCache.put(key, data), DaoCache.put(key, data))

    fun getString(key: String?) =
            Observable.concat(MemoryCache.getString(key), DaoCache.getString(key)).firstElement()

    fun <I> getBean(key: String?, type: Class<I>) =
            Observable.concat(MemoryCache.getBean(key, type), DaoCache.getBean(key, type)).filter { it != null }.firstElement()

    fun <I> getBeanList(key: String?, type: Class<I>) =
            Observable.concat(MemoryCache.getBeanList(key, type), DaoCache.getBeanList(key, type)).filter { it.isNotEmpty() }.firstElement()
}