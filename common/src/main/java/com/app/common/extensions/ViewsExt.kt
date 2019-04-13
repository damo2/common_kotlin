package com.app.common.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout


fun View.setMarginExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
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
        layoutParams = it
    }

}

/**
 * view转成图片
 */
fun View.toBitmapExt(): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    draw(c)
    return b
}

fun View.setPaddingExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    setPadding(left ?: paddingLeft, top ?: paddingTop, right ?: paddingRight, bottom
            ?: paddingBottom)
}


fun View.setWidthHeightExt(width: Int? = null, height: Int? = null) {
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
        layoutParams = it
    }
}

fun View.setWidthScaleExt(width: Int? = null) {
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
        layoutParams = it
    }
}

fun View.getWidthExt(): Int {
    val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(width, height);
    return measuredWidth
}

fun View.getHeightExt(): Int {
    val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(width, height);
    return measuredHeight
}
