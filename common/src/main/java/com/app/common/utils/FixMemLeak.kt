package com.app.common.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field

/**
 * Created by wr
 * Date: 2019/1/15  17:23
 * mail: 1902065822@qq.com
 * describe:
 */
object FixMemLeak {
    private var field: Field? = null
    private var hasField = true

    fun fixLeak(context: Context) {
        if (!hasField) {
            return
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ?: return

        val arr = arrayOf("mLastSrvView")
        for (param in arr) {
            try {
                if (field == null) {
                    field = imm.javaClass.getDeclaredField(param)
                }
                if (field == null) {
                    hasField = false
                }
                field?.isAccessible = true
                field?.set(imm, null)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}
