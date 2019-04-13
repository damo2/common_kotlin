package com.app.common.json

import com.google.gson.*
import com.google.gson.JsonParseException
import java.lang.reflect.Type


/**
 * Gson解析空字符串发生异常的处理方法,再也不会因为后台json字段为空的情况崩溃了
 * Created by wr on 2017/4/21.
 */

fun GsonUtil(): Gson {
    return GsonManager.instance
}

class GsonManager {
    companion object {
        val instance: Gson by lazy {
            GsonBuilder()
                    .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
                    .registerTypeAdapter(Double::class.java, DoubleDefaultAdapter())
                    .registerTypeAdapter(Long::class.java, LongDefaultAdapter())
                    .registerTypeAdapter(String::class.java, StringNullConverter())
                    .create()
        }
    }
}

//"" 转Int 为0
private class IntegerDefaultAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int? =
            if (json.asString == "") 0 else json.asInt


    override fun serialize(src: Int?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = JsonPrimitive(src ?: 0)
}

//"" 转Double 为0.0
private class DoubleDefaultAdapter : JsonSerializer<Double>, JsonDeserializer<Double> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double? =
            if (json.asString == "") 0.0 else json.asDouble


    override fun serialize(src: Double?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = JsonPrimitive(src ?: 0.0)

}

//"" 转Long 为0L
private class LongDefaultAdapter : JsonSerializer<Long>, JsonDeserializer<Long> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Long? =
            if (json.asString == "") 0L else json.asLong


    override fun serialize(src: Long?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = JsonPrimitive(src ?: 0L)
}

//null 转String 为""
private class StringNullConverter : JsonSerializer<String>, JsonDeserializer<String> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): String =
            json.asString ?: ""


    override fun serialize(src: String?, typeOfSrc: Type,
                           context: JsonSerializationContext): JsonElement = JsonPrimitive(src ?: "")
}
