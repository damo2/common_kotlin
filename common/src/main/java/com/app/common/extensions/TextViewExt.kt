package com.app.common.extensions

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.widget.TextView

/**
 * Created by wr
 * Date: 2018/9/25  13:12
 * describe:
 */
fun TextView.setTextColorExt(@ColorRes id: Int) = setTextColor(ContextCompat.getColor(context, id))