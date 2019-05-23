package com.app.common.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.app.common.logger.Logger
import com.app.common.utils.BitmapUtil
import java.util.regex.Pattern

fun String?.equalsExt(str: String?, isIgnoerNull: Boolean = true) = this == str || (isIgnoerNull && (this == null && str == "") || (this == "" && str == null))

fun String?.equalsNotExt(str: String?, isIgnoerNull: Boolean = true) = !equalsExt(str, isIgnoerNull)


fun String?.isEmailExt(): Boolean = !this.isNullOrBlank() && Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(this).matches()

//密码半角（所有英文字符英文符号）
fun String.isPasswordExtHalfAngle(): Boolean = Pattern.compile("^[\u0000-\u00FF]+$").matcher(this).matches()

fun String.toSpannableStringExt(color: Int): SpannableString =
        SpannableString(this).apply { setSpan(ForegroundColorSpan(color), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

//获取String里面的数字转int
fun String.getNumExt(): Int {
    try {
        return Pattern.compile("[^0-9]").matcher(this).replaceAll("").trim().toInt()
    } catch (e: Exception) {
        Logger.d("getNumExt#${this} to Int error")
        return -1
    }
}

//失败返回-1
fun String.toIntExt(): Int {
    try {
        return this.toInt()
    } catch (e: Exception) {
        return -1
    }
}

fun String.imgGetWidthExt() = BitmapUtil.imgGetWidth(this)

fun String.imgGetHeightExt() = BitmapUtil.imgGetHeight(this)

