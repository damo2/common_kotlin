package com.weiyao.zuzuapp

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ListTest {
    @Test
    fun Test() {
        val a = add()
        a()
        a()
        a()
    }

    fun add(): () -> Unit {
        var i = 0
        return { println(i++) }
    }

    fun twoSum(nums: IntArray, target: Int): IntArray {
        nums.forEachIndexed { index, i ->
            val temp = target - i;
            val tempIndex = nums.indexOf(temp)
            if (tempIndex > -1 && index != tempIndex) {
                return intArrayOf(index, tempIndex);
            }
        }
        return intArrayOf()
    }

}
