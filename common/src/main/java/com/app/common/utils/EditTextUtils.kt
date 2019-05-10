package com.app.common.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.app.common.logger.Logger

/**
 * Created by wr
 * Date: 2019/5/10  15:25
 * mail: 1902065822@qq.com
 * describe:
 */
object EditTextUtils {
    /**
     * 禁止输入空格
     */
    fun inhibitInputSpace(editText: EditText) {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (" " == source) "" else null
        }
        editText.filters = arrayOf(filter)
    }

    /**
     * 限制长度
     * @param maxLength 最多输入字数
     * @param outCallback 超出回调
     */
    fun limitLength(editText: EditText,maxLength: Int, outCallback: (()->Unit)? = null) {
        editText.addTextChangedListener(object : TextWatcher {
            private var temp: CharSequence? = null
            private var start: Int = 0
            private var end: Int = 0
            private var countInput: Int = 0

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Logger.d("beforeTextChanged s=" + s.toString() + "\nstart=" + start + " #count=" + count + " #after=" + after)
                temp = s
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                countInput = count
                Logger.d("onTextChanged s=" + s.toString() + "\nstart=" + start + " #before=" + before + " #count=" + count)
            }

            override fun afterTextChanged(s: Editable) {
                Logger.d("afterTextChanged s=" + s.toString())
                start = editText.selectionStart
                end = editText.selectionEnd
                // 先去掉监听器，否则会出现栈溢出
                editText.removeTextChangedListener(this)
                if (s.length > maxLength) {
                    outCallback?.invoke()
                    while (editText.text.toString().length > maxLength) {
                        s.delete(start - 1, end)
                        start--
                        end--
                    }
                }
                editText.text = s
                editText.setSelection(start)
                editText.addTextChangedListener(this)
            }
        })
    }
}