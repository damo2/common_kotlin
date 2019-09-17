package com.app.common.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.app.common.logger.Logger
import java.util.regex.Pattern

/**
 * Created by wr
 * Date: 2019/7/9  16:59
 * mail: 1902065822@qq.com
 * describe:
 */
object StringUtils {
    //是否相等
    fun equals(str1: String?, str2: String?, isIgnoerNull: Boolean = true) = str1 == str2 || (isIgnoerNull && (str1 == null && str2 == "") || (str1 == "" && str2 == null))

    //是否是邮箱
    fun isEmail(str: String?): Boolean = !str.isNullOrBlank() && Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(str).matches()

    fun isMobile(str: String?) :Boolean= !str.isNullOrBlank() && Pattern.compile("[1]\\d{10}").matcher(str).matches()

    //密码半角（所有英文字符英文符号）
    fun isPasswordHalfAngle(str: String?): Boolean = !str.isNullOrBlank() && Pattern.compile("^[\u0000-\u00FF]+$").matcher(str).matches()

    fun setColor(str: String?, color: Int): SpannableString =
            if (!str.isNullOrBlank()) {
                SpannableString(str).apply { setSpan(ForegroundColorSpan(color), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
            } else {
                SpannableString("")
            }

    //获取String里面的数字转int
    fun getNum(str: String?, default: Int): Int {
        return try {
            Pattern.compile("[^0-9]").matcher(str).replaceAll("").trim().toInt()
        } catch (e: Exception) {
            Logger.d("getNumExt#${this} to Int error")
            default
        }
    }

    //失败返回-1
    fun toInt(str: String?, default: Int): Int {
        return try {
            str?.toInt() ?: default
        } catch (e: Exception) {
            default
        }
    }
}