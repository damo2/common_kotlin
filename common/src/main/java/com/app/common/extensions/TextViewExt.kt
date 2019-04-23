package com.app.common.extensions

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by wr
 * Date: 2018/9/25  13:12
 * describe:
 */
fun TextView.setTextColorExt(@ColorRes id: Int) = setTextColor(ContextCompat.getColor(context, id))