package com.app.common.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import java.lang.reflect.Field

/**
 * Created by wr
 * Date: 2019/5/10  15:32
 * mail: 1902065822@qq.com
 * describe:
 */
object ViewUtils {
    fun setMargin(view: View, left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
        val layoutParams = view.layoutParams
        val layout = when (layoutParams) {
            is RelativeLayout.LayoutParams -> layoutParams as RelativeLayout.LayoutParams
            is LinearLayout.LayoutParams -> layoutParams as LinearLayout.LayoutParams
            is FrameLayout.LayoutParams -> layoutParams as FrameLayout.LayoutParams
            is RecyclerView.LayoutParams -> layoutParams as RecyclerView.LayoutParams
            is ViewGroup.MarginLayoutParams -> layoutParams as ViewGroup.MarginLayoutParams
            else -> null
        }
        if (layout == null) return
        val leftResult = left ?: layout.leftMargin
        val rightResult = right ?: layout.rightMargin
        val topResult = top ?: layout.topMargin
        val bottomResult = bottom ?: layout.bottomMargin

        layout.let {
            it.setMargins(leftResult, topResult, rightResult, bottomResult)
            view.layoutParams = it
        }

    }

    /**
     * view转成图片
     */
    fun toBitmap(view: View): Bitmap {
        val b = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        view.draw(c)
        return b
    }

    fun setPadding(view: View, left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
        setPadding(view, left ?: view.paddingLeft, top ?: view.paddingTop, right
                ?: view.paddingRight, bottom
                ?: view.paddingBottom)
    }


    fun setWidthHeight(view: View, width: Int? = null, height: Int? = null) {
        val layoutParams = view.layoutParams
        val layout = when (layoutParams) {
            is RelativeLayout.LayoutParams -> layoutParams as RelativeLayout.LayoutParams
            is LinearLayout.LayoutParams -> layoutParams as LinearLayout.LayoutParams
            is FrameLayout.LayoutParams -> layoutParams as FrameLayout.LayoutParams
            is RecyclerView.LayoutParams -> layoutParams as RecyclerView.LayoutParams
            is ViewGroup.MarginLayoutParams -> layoutParams as ViewGroup.MarginLayoutParams
            else -> null
        }
        if (layout == null) return
        val widthResult = width ?: layout.width
        val heightResult = height ?: layout.height
        layout.let {
            it.width = widthResult
            it.height = heightResult
            view.layoutParams = it
        }
    }

    fun setWidthScale(view: View, width: Int? = null) {
        val layoutParams = view.layoutParams
        val layout = when (layoutParams) {
            is RelativeLayout.LayoutParams -> layoutParams as RelativeLayout.LayoutParams
            is LinearLayout.LayoutParams -> layoutParams as LinearLayout.LayoutParams
            is FrameLayout.LayoutParams -> layoutParams as FrameLayout.LayoutParams
            is RecyclerView.LayoutParams -> layoutParams as RecyclerView.LayoutParams
            is ViewGroup.MarginLayoutParams -> layoutParams as ViewGroup.MarginLayoutParams
            else -> null
        }
        if (layout == null) return
        val widthResult = width ?: layout.width
        val heightResult = view.height ?: layout.height
        layout.let {
            it.width = widthResult
            it.height = heightResult
            view.layoutParams = it
        }
    }

    fun getWidth(view: View): Int {
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height);
        return view.measuredWidth
    }

    fun getHeight(view: View): Int {
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height);
        return view.measuredHeight
    }


    fun setIndicator(tabLayout: TabLayout, left: Int, right: Int) {
        val tabLayoutClass = javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip")
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

}