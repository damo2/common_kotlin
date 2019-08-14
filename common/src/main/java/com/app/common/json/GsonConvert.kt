package com.app.common.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * json数据转对象
 * Created by wangru
 * Date: 2018/4/4  11:08
 * mail: 1902065822@qq.com
 * describe:
 * 主要用于转换有泛型的对象
 * 泛型用GsonConvert，已知类型用扩展属性
 */


inline fun <reified T> gsonTypeExt() = object : TypeToken<T>() {}.type

//eg: json.toBeanExt<User>()
inline fun <reified T : Any> String?.toBeanExt(): T? = Gson().fromJson(this, gsonTypeExt<T>())

//eg: bean.toJsonExt()
fun Any?.toJsonExt(): String? = Gson().toJson(this)

fun <T> String?.toBeanByTypeExt(type: Type): T = Gson().fromJson(this, type)

/**
 * json 转对象  对象有一个泛型
 *
 * @param clazz     要转换成的对象类 eg：BaseBean<ResumeTxtBean>()::class.java
 * @param classType 要转换成的对象的泛型类 eg: ResumeTxtBean::class.java
 * @param <T>       要转换成的对象里面的泛型类名 eg: ResumeTxtBean
 * @return 要转换成的对象类  eg:BaseBean<ResumeTxtBean>()
 * eg: MessageBean<ImageBean> message = GsonConvert.jsonToBean(jsonString, MessageBean.class, ImageBean.class);
 */
fun <T> String?.toBeanByClass(clazz: Class<T>, classType: Class<*>): T? {
    val typeT = ParameterizedTypeImpl(clazz, arrayOf(classType))
    return Gson().fromJson<T>(this, typeT)
}

/**
 * json 转List对象  对象里面有一个泛型
 *
 * @param json      json 数据
 * @param classType List对象的泛型类
 * @param <T>       List对象的泛型类名
 * @return List对象
 * eg: var  bean:List<Bean>?  = GsonConvert.jsonToBeanList(jsonStr, Bean::class.java)
</I></T> */
fun <T> String?.toBeanListExt(classType: Class<T>): List<T>? {
    val listType = ParameterizedTypeImpl(List::class.java, arrayOf(classType))
    return Gson().fromJson<List<T>>(this, listType)
}

/***
 *
 * @param json json 数据
 * @param classType eg:BaseBean::class.java
 * @param clazz eg: SelectTypeBeam::class.java
 * @return BaseBean<SelectTypeBeam>
 *     eg: var  bean:BaseBean<SelectTypeBeam>? = GsonConvert.fromJsonToBeanDataList(jsonStr, BaseBean::class.java, SelectTypeBeam::class.java)
 */
fun <T> String?.toBeanDataListExt(classType: Class<*>, clazz: Class<T>): Any? {
    // 生成List<T> 中的 List<T>
    val listType = ParameterizedTypeImpl(List::class.java, arrayOf(clazz))
    // 根据List<T>生成完整的Result<List<T>>
    val type = ParameterizedTypeImpl(classType, arrayOf(listType))
    return Gson().fromJson<Any>(this, type)
}

class ParameterizedTypeImpl(private val raw: Class<*>, args: Array<Type>?) : ParameterizedType {
    private val args: Array<Type> = args ?: arrayOf()

    override fun getActualTypeArguments(): Array<Type> = args

    override fun getRawType(): Type = raw

    override fun getOwnerType(): Type? = null
}
