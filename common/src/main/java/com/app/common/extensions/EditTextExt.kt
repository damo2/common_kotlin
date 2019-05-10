package com.app.common.extensions

import android.widget.EditText
import com.app.common.utils.EditTextUtils

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