package com.app.common.adapter.util

/**
 * Created by zhy on 16/6/22.
 */
interface ItemViewDelegate<T> {

    fun getItemViewLayoutId(): Int

    fun isForViewType(item: T, position: Int): Boolean

    fun convert(holder: ViewHolder, item: T, position: Int, payloads: List<Any>?)

}
