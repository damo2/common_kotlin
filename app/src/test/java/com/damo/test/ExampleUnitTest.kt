package com.damo.test

import org.junit.Test

import org.junit.Assert.*

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
        open class C(open val name:String)
        open class Java(override val name: String):C("java")
        class Kotlin(override val name: String):Java("kotlin")

        val c = C("c")
        var cList:ArrayList<out C> = arrayListOf(c) // <out C>

        val java = Java("java")
        var javaList:ArrayList<Java> = arrayListOf(java)

        cList = javaList
        cList.add(java) //set
        cList[0].name  //get

        for ((index,value) in cList.withIndex()) {
            println("$index - ${value.name}")
        }
    }
}
