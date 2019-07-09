package com.app.common.extensions

import android.text.SpannableString
import com.app.common.utils.BitmapUtil
import com.app.common.utils.StringUtils

//是否相等
fun String?.equalsExt(str: String?, isIgnoerNull: Boolean = true) = StringUtils.equals(this, str, isIgnoerNull)

fun String?.equalsNotExt(str: String?, isIgnoerNull: Boolean = true) = !equalsExt(str, isIgnoerNull)

//是否是邮箱
fun String?.isEmailExt(): Boolean = StringUtils.isEmail(this)

//密码半角（所有英文字符英文符号）
fun String?.isPasswordHalfAngleExt(): Boolean = StringUtils.isPasswordHalfAngle(this)

//String 设置颜色
fun String?.setColorExt(color: Int): SpannableString = StringUtils.setColor(this, color)

//图片路径获取宽
fun String.imgGetWidthExt() = BitmapUtil.imgGetWidth(this)

//图片路径获取高
fun String.imgGetHeightExt() = BitmapUtil.imgGetHeight(this)

//获取String里面的数字转int
fun String?.getNumExt(default: Int = -1): Int = StringUtils.getNum(this, default)

//失败返回-1
fun String?.toIntExt(default: Int = -1): Int = StringUtils.toInt(this, default)

