package com.app.common.extensions

/**
 * Created by wr
 * Date: 2018/9/17  16:36
 * describe:
 */


fun List<Any>?.joinExt(str: String): String {
    var result = ""
    this?.let {
        forEachIndexed { index, s ->
            result += ((if (index == 0) "" else str) + s)
        }
    }
    return result
}


fun <E> List<E>.getExt(index: Int): E? {
    try {
        return this.get(index)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun <E> List<E>?.isEmptyExt(): Boolean = this == null || this.isEmpty()
