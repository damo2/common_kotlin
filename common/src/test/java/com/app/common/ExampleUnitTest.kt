package com.app.common

import com.app.common.encrypt.type.RSAEncrypt
import com.app.common.encrypt.type.RSAUtils
import com.app.common.json.toBeanExt
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

        @SerializedName("son")
        var son: User? = null
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

        val user0 = json.toBeanExt<User>()
        println(Gson().toJson(user0))

        val json2 = "{\n" +
                "    \"isGirl\": \"a\"\n" +
                "}"

        try {
            val user2 = json.toBeanExt<User>()
            println(Gson().toJson(user2))
            println("isGirl=${user2?.isGirl}")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val user3 = json.toBeanExt<User>()
            println(Gson().toJson(user3))
            println("isGirl=${user3?.isGirl}")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val userbeanSonSon = User().apply {
            name = "zhuan"
            age = 20
        }
        val userbeanSon = User().apply {
            name = "zhuan"
            age = 40
            son = userbeanSonSon
        }
        val userbean = User().apply {
            name = "zhuan"
            age = 60
            son = userbeanSon
        }

        val user = userbean.toJsonExt().toBeanExt<User>()
        println(Gson().toJson(user))

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
        val message = json.toBeanExt<MessageBean<ImageBean>>()
        val messageList = jsonList.toBeanExt<List<MessageBean<ImageBean>>>()
        print("message#" + message)
        print("messageList#" + messageList)
    }

    @Test
    @Throws(Exception::class)
    fun testEncrypt() {

        val data = " 我的内心毫无波动,据说是后台生成的密文是一堆乱码 ,我也没功夫研究后台日至输出,猜测是编码问题,我的内心毫无波动,据说是后台生成的密文是一堆乱码,我也没功夫研究后台日至输出,猜测是编码问题,"


        //生成密钥对
        val keyPair = RSAUtils.generateRSAKeyPair(1024)!!
        //获取公钥
        val publicKey = RSAUtils.getPublicKey(keyPair)
        //获取私钥
        val privateKey = RSAUtils.getPrivateKey(keyPair)

        //用公钥加密
        val encrypt1 = RSAEncrypt.encrypt(data, publicKey)
        println("加密后：$encrypt1");
        //用私钥解密
        val decrypt1 = RSAEncrypt.decrypt(encrypt1, privateKey)
        println("解密后：$decrypt1");
    }


}