package com.weiyao.zuzuapp

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ListTest {
    @Test
    fun listTest() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        assertEquals(listOf(Pair(1, 7), Pair(2, 8)), list.zip(listOf(7, 8)))
    }

    fun strStr(haystack: String, needle: String): Int {
        val index = haystack.indexOf(needle)
        return  if (index > -1) index else 0
    }
    
}
