package com.damo.libdb

import com.damo.libdb.cache.DaoCache
import com.google.gson.Gson
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @see T String、对象。 不存int、boolean、long之类的
 */
class Dao<T>(var key: String, var type: Type, private var default: T? = null) : ReadWriteProperty<Any?, T?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = Gson().fromJson(DaoCache.getStringSync(key), type)
            ?: default

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value == null) {
            DaoCache.deleteSync(key)
        } else {
            DaoCache.putSync(key, value)
        }
    }
}