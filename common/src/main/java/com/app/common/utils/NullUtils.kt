package com.app.common.utils

/**
 * Created by wr
 * Date: 2019/5/9  16:30
 * mail: 1902065822@qq.com
 * describe:
 * 判断不为空
 */

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun <T1, T2, T3> ifNotNull(value1: T1?, value2: T2?, value3: T3?, bothNotNull: (T1, T2, T3) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null) {
        bothNotNull(value1, value2, value3)
    }
}

fun <T1, T2, T3, T4> ifNotNull(value1: T1?, value2: T2?, value3: T3?, value4: T4?, bothNotNull: (T1, T2, T3, T4) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null && value4 != null) {
        bothNotNull(value1, value2, value3, value4)
    }
}

fun <T1, T2, T3, T4, T5> ifNotNull(value1: T1?, value2: T2?, value3: T3?, value4: T4?, value5: T5?, bothNotNull: (T1, T2, T3, T4, T5) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null && value4 != null && value5 != null) {
        bothNotNull(value1, value2, value3, value4, value5)
    }
}