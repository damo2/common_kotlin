package com.damo.test

import com.app.common.utils.ifNotNull
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testFanxin() {
        open class C(open val name: String)
        open class Java(override val name: String) : C("java")
        class Kotlin(override val name: String) : Java("kotlin")

        val c = C("c")
        var cList: ArrayList<out C> = arrayListOf(c) // <out C>

        val java = Java("java")
        var javaList: ArrayList<Java> = arrayListOf(java)

        cList = javaList
        cList.add(java) //set
        cList[0].name  //get

        for ((index, value) in cList.withIndex()) {
            println("$index - ${value.name}")
        }
    }


    @Test
    fun testList() {
        data class People(val name: String, val age: Int, var account: List<String>? = null)

        val peopleList = listOf(
                People("小二", 13, listOf("小er", "xiaoer")),
                People("张三", 13, listOf("张san", "zhuangsan")),
                People("李四", 15),
                People("王五", 18),
                People("赵六", 22),
                People("沈七", 22),
                People("胡八", 25),
                People("胡八", 22)
        )

        //是否成年判断式
        val isChengnian = { p: People -> p.age >= 18 }
        //是否全部成年
        val isChengnianAll = peopleList.all(isChengnian)
        //是否有成年人
        val isChengnianHave = peopleList.any(isChengnian)
        //成年人数量
        val chengnianNum = peopleList.count(isChengnian)
        //按照年龄分组
        val peopleGroup = peopleList.groupBy { it.age }
        //所有账户
        val accountAll = peopleList.flatMap { it.account ?: listOf() }

        println("isChengnianAll=$isChengnianAll  isChengnianHave=$isChengnianHave  chengnianNum=$chengnianNum" +
                "\npeopleGroup=$peopleGroup" +
                "\naccountAll=$accountAll")

        //结果 isChengnianAll=false  isChengnianHave=true  chengnianNum=4

        fun buildString(action: StringBuilder.() -> Unit): String {
            val stringBuilder = StringBuilder()
            stringBuilder.action()
            return stringBuilder.toString()
        }

        val str = buildString {
            append("a")
            append("b")
        }
        val num1: Int? = null
        val num2: Int? = 2
        val num3: Int? = 3
        ifNotNull(num1, num2, num3) { num1a, num2a, num3a ->
            print(num1a + num2a + num3a)
        }
    }
}
