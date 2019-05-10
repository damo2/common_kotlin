package com.app.common.extensions

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import com.app.common.logger.Logger
import com.app.common.utils.EditTextUtils
import com.app.common.view.ToastX

/**
 * Created by wr
 * Date: 2018/8/29  16:34
 * describe:
 */

/**
 * 禁止输入空格
 */
fun EditText.inhibitInputSpaceExt() = EditTextUtils.inhibitInputSpace(this)

/**
 * 限制长度
 * @param maxLength 最多输入字数
 * @param outCallback 超出回调
 */
fun EditText.limitLengthExt(maxLength: Int, outCallback: (()->Unit)? = null) =EditTextUtils.limitLength(this,maxLength,outCallback)