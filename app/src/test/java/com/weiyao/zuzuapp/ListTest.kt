package com.weiyao.zuzuapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import org.junit.Test
import kotlin.coroutines.coroutineContext

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ListTest {
    @Test
    fun Test() {
        print(twoSum(intArrayOf(3, 2, 4), 6).toList().toString())
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
