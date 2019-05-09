package com.app.common.extensions

/**
 * Created by wr
 * Date: 2018/9/17  16:36
 * describe:
 */

fun <E> List<E>.getExt(index: Int): E? {
    try {
        return this.get(index)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun <E> List<E>?.isEmptyExt(): Boolean = this == null || this.isEmpty()
