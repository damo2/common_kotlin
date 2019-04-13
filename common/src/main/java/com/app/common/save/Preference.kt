package com.app.common.save

import android.content.Context
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 封装 SharedPreferences 序列化和反序列化来存储数据
 * 注意要在全局环境使用
 * Created by wangru
 * Date: 2018/7/4  13:52
 * mail: 1902065822@qq.com
 * describe:
 * 属性将自己的get和set方法委托给了这个类的getValue和setValue。
 * eg:
 * var save : UserInfo by Preference<UserInfo>(this,"userinfo", UserInfo("zhuangshan","123"))
 * @see T 必须添加 serialVersionUID
 */
class Preference<T : Serializable>(val context: Context, val key: String, val default: T, val fileName: String = "app") : ReadWriteProperty<Any?, T> {
    val prefs by lazy { context.getSharedPreferences(fileName, Context.MODE_PRIVATE) }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(key, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(key, value)
    }


    private fun <A> findPreference(name: String, default: A): A = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialization(getString(name, serialize(default)))
        }
        res as A
    }

    private fun <A> putPreference(key: String, value: A) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key, serialize(value))
        }.apply()
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        prefs.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * 序列化对象
     * @param person
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <A> serialize(obj: A?): String? {
        if (obj == null) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象

     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}