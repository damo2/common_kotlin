package com.app.common.extensions

import android.app.Dialog
import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.common.utils.ActivityUtils
import com.app.common.utils.DialogUtil
import com.app.common.utils.RecyclerViewUtils
import com.app.common.utils.ViewUtils
import com.google.android.material.tabs.TabLayout

fun View.setMarginExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) = ViewUtils.setMargin(this, left, top, right, bottom)

//view转成图片
fun View.toBitmapExt(): Bitmap = ViewUtils.toBitmap(this)

fun View.setPaddingExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) = ViewUtils.setPadding(this, left, top, right, bottom)

fun View.setWidthHeightExt(width: Int? = null, height: Int? = null) = ViewUtils.setWidthHeight(this, width)

fun View.setWidthScaleExt(width: Int? = null) = ViewUtils.setWidthScale(this, width)

fun View.getWidthExt(): Int = ViewUtils.getWidth(this)

fun View.getHeightExt() = ViewUtils.getHeight(this)


fun View.getActivityExt() = ActivityUtils.getActivityByView(this)

//TabLayout
fun TabLayout.setIndicatorExt(leftDp: Int, rightDp: Int) = ViewUtils.setIndicator(this, leftDp, rightDp)


fun RecyclerView.addScrollPauseLoadExt() = RecyclerViewUtils.addScrollPauseLoad(this)


fun Dialog.showExt() = DialogUtil.safeShowDialog(this)
fun Dialog.dismissExt() = DialogUtil.safeDismissDialog(this)
