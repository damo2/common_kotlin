package com.app.common.extensions

import android.support.design.widget.TabLayout
import android.widget.LinearLayout
import java.lang.reflect.Field

/**
 * Created by wr
 * Date: 2018/8/22  15:16
 * describe:
 */
fun TabLayout.setIndicator(leftDp: Int, rightDp: Int) {
    val tabLayout = javaClass
    var tabStrip: Field? = null
    try {
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }

    tabStrip?.isAccessible = true
    var llTab: LinearLayout? = null
    try {
        llTab = tabStrip?.get(this) as LinearLayout
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

    val left = context.dp2px(leftDp)
    val right = context.dp2px(rightDp)
    llTab?.let {
        for (i in 0 until llTab.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }
    }

}