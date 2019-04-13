package com.app.common.extensions

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import com.app.common.logger.Logger
import com.app.common.view.ToastX

/**
 * Created by wr
 * Date: 2018/8/29  16:34
 * describe:
 */

//禁止输入空格
fun EditText.inhibitInputSpaceExt() {
    val filter = object : InputFilter {
        //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? =
                if (" ".equals(source)) "" else null
    }
    filters = arrayOf(filter)
}

//限制长度
fun EditText.limitLengthExt(context: Context, maxLength: Int, tig: String? = null) {
    addTextChangedListener(object : TextWatcher {
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
            start = selectionStart
            end = selectionEnd
            // 先去掉监听器，否则会出现栈溢出
            removeTextChangedListener(this)
            if (s.length > maxLength) {
                tig?.let { ToastX(context).info(it) }
                while (text.toString().length > maxLength) {
                    s.delete(start - 1, end)
                    start--
                    end--
                }
            }
            text = s
            setSelection(start)
            addTextChangedListener(this)
        }
    })
}