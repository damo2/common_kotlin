package com.app.common.extensions

import android.util.SparseArray
import java.util.*

/**
 * Created by wr
 * Date: 2019/10/14  9:51
 * mail: 1902065822@qq.com
 * describe:
 */

//防止数组越界
fun <T> List<T>?.getExt(index: Int?): T? {
    if (this == null) return null
    if (index == null) return null
    val isOverstep = index < 0 || index > size - 1
    if (isOverstep) return null
    return get(index)
}


fun <T> List<T>.toSparseArrayExt(): SparseArray<T> {
    val sparseArray = SparseArray<T>()
    this.forEachIndexed { index, t ->
        sparseArray.put(index, t)
    }
    return sparseArray
}

fun <T> SparseArray<T>.toArrayListExt(): ArrayList<T> {
    val arrayList = arrayListOf<T>()
    val size = this.size()
    for (i in 0 until size) {
        if (size != this.size()) throw ConcurrentModificationException()
        arrayList.add(this.valueAt(i))
    }
    return arrayList
}