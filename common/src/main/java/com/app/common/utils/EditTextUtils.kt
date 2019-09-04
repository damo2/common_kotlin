package com.app.common.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
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
        val filter = InputFilter { source, _, _, _, _, _ ->
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (" " == source) "" else null
        }
        editText.filters = arrayOf(filter)
    }

    /**
     * 限制长度
     * @param maxLength 最多输入字数
     * @param outCallback 字数超出限制回调
     */
    fun limitLength(editText: EditText, maxLength: Int, outCallback: (() -> Unit)? = null) {
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

    /**
     * EditText设置只能输入数字和小数点，小数点只能1个且小数点后最多只能2位
     */
    fun setOnlyDecimal(editText: EditText) {
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        editText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //这部分是处理如果输入框内小数点后有俩位，那么舍弃最后一位赋值，光标移动到最后
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        editText.setText(s.toString().subSequence(0, s.toString().indexOf(".") + 3))
                        editText.setSelection(s.toString().subSequence(0, s.toString().indexOf(".") + 3).length)
                    }
                }

                if (s.toString().trim().substring(0) == ".") {
                    editText.setText("0$s")
                    editText.setSelection(2)
                }

                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (s.toString().substring(1, 2) != ".") {
                        editText.setText(s.subSequence(0, 1))
                        editText.setSelection(1)
                        return
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }

        })

    }

    /**
     * EditText设置密码显示或隐藏
     */
    fun setPwdShowOrHind(edt: EditText, isShow: Boolean) {
        val pos = edt.selectionStart
        edt.transformationMethod = if (isShow) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        edt.setSelection(pos)
    }
}