package com.damo.libdb.cache


import com.damo.libdb.cache.GsonConvert.jsonToBeanList
import com.damo.libdb.objectbox.bean.CacheInfoBean
import com.damo.libdb.objectbox.ope.CacheInfoBoxOpe
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 缓存数据和取缓存数据
 * Created by wr
 * Date: 2018/5/10  15:26
 * mail: 1902065822@qq.com
 * describe:
 */

object DaoCache : ICache {
    /*******************************************************  异步   ***********************************************/
    //保存
    private fun getSaveCacheCommonObservable(cacheKey: String?, infoCall: () -> String): Observable<Boolean> =
            Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                CacheInfoBean().apply {
                    timeCreate = System.currentTimeMillis()
                    key = cacheKey
                    data = infoCall()
                }.let {
                    val result = CacheInfoBoxOpe.addOrUpdateByKey(cacheKey ?: "", it) > 0
                    emitter.onNext(result)
                }
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    //查询
    private fun <I> getQueryCacheCommonObservable(cacheKey: String?, change: (result: String) -> I): Observable<I> =
            Observable.create(ObservableOnSubscribe<String> { emitter ->
                CacheInfoBoxOpe.getDataByKey(cacheKey ?: "")?.let { emitter.onNext(it) }
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        change(it)
                    }


    //删除
    private fun getDeleteCacheCommonObservable(cacheKey: String?): Observable<Boolean> =
            Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
                val result = CacheInfoBoxOpe.deleteByKey(cacheKey ?: "") > 0
                emitter.onNext(result)
                emitter.onComplete()
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    //保存
    override fun put(key: String?, data: Any?): Observable<Boolean> =
            getSaveCacheCommonObservable(key, { Gson().toJson(data) })

    //查询
    override fun getString(key: String?) =
            getQueryCacheCommonObservable(key, { it })

    override fun <I> getBean(key: String?, type: Class<I>) =
            getQueryCacheCommonObservable(key, { Gson().fromJson(it, type) })

    override fun <I> getBeanList(key: String?, type: Class<I>) =
            getQueryCacheCommonObservable(key, { GsonConvert.jsonToBeanList(it, type) })

    //删除
    override fun delete(key: String?) =
            getDeleteCacheCommonObservable(key)

    /********************************************************  同步   ***************************************************/

    //同步方法保存
    private fun saveCacheCommonSynch(cacheKey: String, infoCall: () -> String) =
            CacheInfoBean().apply {
                timeCreate = System.currentTimeMillis()
                key = cacheKey
                data = infoCall()
            }.let {
                CacheInfoBoxOpe.addOrUpdateByKey(cacheKey, it) > 0
            }


    //同步方法查询
    override fun getStringSync(key: String?): String? = CacheInfoBoxOpe.getDataByKey(key ?: "")

    override fun putSync(key: String?, data: Any?) = saveCacheCommonSynch(key
            ?: "", { Gson().toJson(data) })

    override fun <I> getBeanSync(key: String?, type: Class<I>): I = Gson().fromJson(getStringSync(key), type)

    override fun <I> getBeanListSync(key: String?, type: Class<I>): List<I>? = jsonToBeanList(getStringSync(key), type)

    override fun deleteSync(key: String?) = CacheInfoBoxOpe.deleteByKey(key ?: "") > 0
}

private object GsonConvert {

    fun <T> jsonToBeanList(json: String?, classType: Class<T>): List<T>? {
        val listType = ParameterizedTypeImpl(List::class.java, arrayOf(classType))
        return Gson().fromJson<List<T>>(json, listType)
    }

    @Deprecated("  val resultData = GsonConvert.fromJsonToBeanDataList(result,BaseBean::class.java,SelectTypeTwoBean::class.java) as BaseBean<List<SelectTypeTwoBean>>")
    fun <T> fromJsonToBeanDataList(json: String?, classType: Class<*>, clazz: Class<T>): Any? {
        // 生成List<T> 中的 List<T>
        val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
        // 根据List<T>生成完整的Result<List<T>>
        val type = ParameterizedTypeImpl(classType, arrayOf(listType))
        return Gson().fromJson<Any>(json, type)
    }

    class ParameterizedTypeImpl(private val raw: Class<*>, args: Array<Type>?) : ParameterizedType {
        private val args: Array<Type> = args ?: arrayOf()

        override fun getActualTypeArguments(): Array<Type> = args

        override fun getRawType(): Type = raw

        override fun getOwnerType(): Type? = null
    }
}
