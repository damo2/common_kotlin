package com.app.common

import com.app.common.json.gsonFromJsonExt
import com.app.common.json.toJsonExt
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    internal inner class User {
        /**
         * id : 1001
         * name : Test
         * age : 20
         */
        @SerializedName("id")
        var id: String = ""
        @SerializedName("name")
        var name: String? = null

        @SerializedName("age")
        var age: Int = 0
        @SerializedName("type")
        var type: Int? = 0

        @SerializedName("isGirl")
        var isGirl: Boolean = false
        @SerializedName("isUser")
        var isUser: Boolean = false
    }

    @Test
    @Throws(Exception::class)
    fun testGson() {
        val json = "{\n" +
                "    \"id\": \"1001\",\n" +
                "    \"name\": \"Test\",\n" +
                "    \"age\": 20,\n" +
                "    \"type\": 1,\n" +
                "    \"isGirl\": true\n" +
                "}"
        val user = json.gsonFromJsonExt<User>()
        println(Gson().toJson(user))

        val json2 = "{\n" +
                "    \"isGirl\": \"a\"\n" +
                "}"

        try {
            val user2 = json.gsonFromJsonExt<User>()
            println(Gson().toJson(user2))
            println("isGirl=${user2?.isGirl}")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val user3 = json.gsonFromJsonExt<User>()
            println(Gson().toJson(user3))
            println("isGirl=${user3?.isGirl}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    class MessageBean<T>() {
        var image: T? = null
        var msg: String? = null
    }

    class ImageBean() {
        var path: String? = null
    }


    @Test
    @Throws(Exception::class)
    fun testGsonCover() {
        val msg = MessageBean<ImageBean>()
        msg.image = ImageBean()
        msg.image!!.path = "http"
        msg.msg = "msg"

        val list = listOf(msg, msg, msg, msg)
        val jsonList = list.toJsonExt()
        val json = msg.toJsonExt()
        val message =json.gsonFromJsonExt<MessageBean<ImageBean>>()
        val messageList =jsonList.gsonFromJsonExt<List<MessageBean<ImageBean>>>()
        print("message#" +message)
        print("messageList#" +messageList)
    }

}