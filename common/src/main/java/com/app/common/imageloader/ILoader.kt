package com.app.common.imageloader

import android.content.Context
import android.widget.ImageView
import com.app.common.extensions.dp2px

interface ILoader {
    /**
     * @param isCircle 是否是圆形图片
     * 加载图片
     */
    fun load(context: Any, model: Any? = "", target: ImageView, placeholder: Int? = null, error: Int? = null, centerCrop: Boolean = true, thumbSize: Float = 0f, cacheType: Int = CacheType.RESOURCE, isCircleCrop: Boolean = true, roundRadius: Int ?=null)

    /**
     * 清除内存缓存
     */
    fun clearMemory(context: Any)

}

object CacheType {
    val NONE = -1
    val All = 0
    val DATA = 1
    val RESOURCE =2

}