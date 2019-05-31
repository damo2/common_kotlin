package com.damo.libdb.cache

import io.reactivex.Observable

/**
 * Created by wr
 * Date: 2019/1/24  14:25
 * mail: 1902065822@qq.com
 * describe:
 */
interface ICache {

    //异步
    fun put(key: String?, data: Any?): Observable<Boolean>
    fun getString(key: String?): Observable<String>
    fun <I> getBean(key: String?, type: Class<I>): Observable<I?>
    fun <I> getBeanList(key: String?, type: Class<I>): Observable<List<I>?>
    fun delete(key: String?): Observable<Boolean>
    //同步
    fun putSync(key: String?, data: Any?): Boolean
    fun getStringSync(key: String?): String?
    fun <I> getBeanSync(key: String?, type: Class<I>): I?
    fun <I> getBeanListSync(key: String?, type: Class<I>): List<I>?
    fun deleteSync(key: String?): Boolean
}