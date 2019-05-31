package com.damo.libdb.objectbox.ope

import android.util.Log
import com.damo.libdb.objectbox.ObjectBoxInit
import com.damo.libdb.objectbox.bean.CacheInfoBean
import com.damo.libdb.objectbox.bean.CacheInfoBean_

import io.objectbox.Box
import io.objectbox.android.AndroidScheduler
import io.objectbox.kotlin.query

/**
 * Created by wangru
 * Date: 2018/12/27  14:21
 * mail: 1902065822@qq.com
 * describe:
 * {@see <a href="https://docs.objectbox.io/queries">objectbox</a>}
 */
object CacheInfoBoxOpe {
    private val cacheInfoBox: Box<CacheInfoBean> by lazy { ObjectBoxInit.boxStore.boxFor(CacheInfoBean::class.java) }

    //添加或更新
    fun addOrUpdateByKey(key: String, cacheInfo: CacheInfoBean) = cacheInfoBox.put(cacheInfo.apply {
        getByKey(key)?.let { id = it.id }
    })

    fun getDataByKey(key: String) = getByKey(key)?.data

    fun deleteByKey(key: String) = cacheInfoBox.query {
        equal(CacheInfoBean_.key, key)
        build()
    }.remove()

    private fun getByKey(key: String) = cacheInfoBox.query().equal(CacheInfoBean_.key, key).build().findFirst()

    fun test(key: String) {
        val query = cacheInfoBox.query().equal(CacheInfoBean_.key, key).build()
        val subscription = query.subscribe().on(AndroidScheduler.mainThread())
                .observer { result -> Log.d("","${result.firstOrNull()?.data}") }
        cacheInfoBox.query().build().property(CacheInfoBean_.key)?.findString()?.let {
            Log.d("", it)
        }
    }
}