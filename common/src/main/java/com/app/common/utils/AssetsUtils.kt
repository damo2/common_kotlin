package com.app.common.utils

import android.content.Context

/**
 * Assets 读取
 * Created by wr
 * Date: 2018/9/3  9:17
 * describe:
 */
object AssetsUtils {
    fun getTxt(context: Context, fileName: String): String = context.assets.open(fileName).bufferedReader().use { it.readText() }
}
