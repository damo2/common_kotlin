package com.app.common.save

import android.content.Context
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 封装 SharedPreferences 序列化和反序列化来存储数据(取值能为空)
 * 注意要在全局环境使用
 * Created by wangru
 * Date: 2018/7/4  13:52
 * mail: 1902065822@qq.com
 * describe:
 * 属性将自己的get和set方法委托给了这个类的getValue和setValue。
 * eg:
 * var save : UserInfo? by Preference<UserInfo>(this,"userinfo", UserInfo("zhuangshan","123"))
 * @see T 必须添加 serialVersionUID
 */
class Preference<T : Serializable> private constructor(val context: Context, val key: String, val clazz: Class<T>, val default: T? = null, val fileName: String = "app") : ReadWriteProperty<Any?, T?> {

    constructor(context: Context, key: String, clazz: Class<T>, fileName: String = "app") : this(context, key, clazz, null, fileName)
    constructor(context: Context, key: String, default: T, fileName: String = "app") : this(context, key, default.javaClass, default, fileName)

    val prefs by lazy { context.getSharedPreferences(fileName, Context.MODE_PRIVATE) }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return findPreference(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        putPreference(key, value)
    }


    private fun findPreference(name: String): T? = with(prefs) {
        val res: Any = when (clazz.simpleName.toLowerCase()) {
            Long::class.java.simpleName.toLowerCase() -> getLong(name, if (default is Long) default else 0L)
            String::class.java.simpleName.toLowerCase() -> getString(name, if (default is String) default else null)
            Int::class.java.simpleName.toLowerCase() -> getInt(name, if (default is Int) default else 0)
            Boolean::class.java.simpleName.toLowerCase() -> getBoolean(name, if (default is Boolean) default else false)
            Float::class.java.simpleName.toLowerCase() -> getFloat(name, if (default is Float) default else 0f)
            else -> deSerialization(getString(name, serialize(default)))
        }
        res as? T
    }

    private fun <A> putPreference(key: String, value: A?) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            value == null -> return@with
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
    private fun <A> deSerialization(str: String?): A? {
        if (str == null) return str
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as? A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}