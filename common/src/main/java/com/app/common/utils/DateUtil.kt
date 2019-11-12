package com.app.common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wr
 * Date: 2019/8/9  16:40
 * mail: 1902065822@qq.com
 * describe:
val date = Date()
date + 1 //后一天
date - 1 //前一天
date + Month(2) //后2月
date - Year(3) //前3年
date++  //本月的最后一天
date--  //本月的第一天
取年月日时分秒 date[0]  date[1] date[2] 。。。
//日期比较
if( date1 > date2){

}
 */
enum class DateOptUnit {
    YEAR, MONTH, DATE, HOUR, MINUTE, SECOND;

    fun parseType(): Int = when (this) {
        YEAR -> Calendar.YEAR
        MONTH -> Calendar.MONTH
        DATE -> Calendar.DATE
        HOUR -> Calendar.HOUR_OF_DAY
        MINUTE -> Calendar.MINUTE
        SECOND -> Calendar.SECOND
    }
}

data class DateOperator(val unit: DateOptUnit, val value: Int)

//年 year(2019)
fun Any.year(value: Int): DateOperator {
    return DateOperator(DateOptUnit.YEAR, value)
}

//月 month(8)
fun Any.month(value: Int): DateOperator {
    return DateOperator(DateOptUnit.MONTH, value)
}

//日 day(8)
fun Any.day(value: Int): DateOperator {
    return DateOperator(DateOptUnit.DATE, value)
}

//时 hour(8)
fun Any.hour(value: Int): DateOperator {
    return DateOperator(DateOptUnit.HOUR, value)
}

//分 minute(8)
fun Any.minute(value: Int): DateOperator {
    return DateOperator(DateOptUnit.MINUTE, value)
}

//秒 second(8)
fun Any.second(value: Int): DateOperator {
    return DateOperator(DateOptUnit.SECOND, value)
}

/**
 * date+1
 * 往后的几天
 */
operator fun Date.plus(nextVal: Int): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.DATE, nextVal)
    return calendar.time
}

/**
 * date-1
 * 往前的几天
 */
operator fun Date.minus(nextVal: Int): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.DATE, nextVal * -1)
    return calendar.time
}

/**
 * date+year(3)
 * 往后的几天
 */
operator fun Date.plus(nextVal: DateOperator): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(nextVal.unit.parseType(), nextVal.value)
    return calendar.time
}

/**
 * date-month(4)
 * 往前的几天
 */
operator fun Date.minus(nextVal: DateOperator): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(nextVal.unit.parseType(), nextVal.value * -1)
    return calendar.time
}

/**
 * 得到月末
 */
operator fun Date.inc(): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, 0);
    return calendar.time
}

/**
 * 得到月初
 */
operator fun Date.dec(): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

/**
 * 取 年月日时分秒 0 - 5
 * 例如 2015-12-21 22:15:56
 * date[0]:2015  date[1]:12 date[2]:21 date[3]:22 date[4]:15 date[5]:56
 */
operator fun Date.get(position: Int): Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    return when (position) {
        0 -> calendar.get(Calendar.YEAR)
        1 -> calendar.get(Calendar.MONTH) + 1
        2 -> calendar.get(Calendar.DAY_OF_MONTH)
        3 -> calendar.get(Calendar.HOUR_OF_DAY)
        4 -> calendar.get(Calendar.MINUTE)
        5 -> calendar.get(Calendar.SECOND)
        6 -> calendar.get(Calendar.MILLISECOND)
        else -> 0
    }
}

/**
 * 比较2个日期
 * if(date1 > date2) {
 * }
 */
operator fun Date.compareTo(compareDate: Date): Int {
    return (time - compareDate.time).toInt()
}

/**
 * 日期转化为字符串
 */
fun Date.stringFormat(formatType: String = "yyyy/MM/dd HH:mm:ss"): String {
    return SimpleDateFormat(formatType, Locale.US).format(this)
}

fun Int.toAddZeroExt() = if (this < 10) "0$this" else "$this"
