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
fun EditText.limitLengthExt(maxLength: Int, outCallback: (() -> Unit)? = null) = EditTextUtils.limitLength(this, maxLength, outCallback)

/**
 * EditText设置只能输入数字和小数点，小数点只能1个且小数点后最多只能2位
 */
fun EditText.setOnlyDecimalExt() = EditTextUtils.setOnlyDecimal(this)

/**
 * EditText设置密码显示或隐藏
 */
fun EditText.setPwdShowOrHindExt(isShow: Boolean) = EditTextUtils.setPwdShowOrHind(this, isShow)