package com.app.common.extensions

import android.app.Dialog
import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.common.R
import com.app.common.utils.ActivityUtils
import com.app.common.utils.DialogUtil
import com.app.common.utils.RecyclerViewUtils
import com.app.common.utils.ViewUtils
import com.google.android.material.tabs.TabLayout

val View.widthExt: Int
    get() = ViewUtils.getWidth(this)

val View.heightExt: Int
    get() = ViewUtils.getHeight(this)

/***
 * 防止快速点击
 * @param time Long 间隔，默认500毫秒
 */
fun <T : View> T.setOnClickExtNoFast(time: Long = 300L, block: (view: View) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(R.id.idTriggerLastTime) != null) getTag(R.id.idTriggerLastTime) as Long else 0
    set(value) {
        setTag(R.id.idTriggerLastTime, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.idTriggerDelay) != null) getTag(R.id.idTriggerDelay) as Long else -1
    set(value) {
        setTag(R.id.idTriggerDelay, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}


fun View.setMarginExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) = ViewUtils.setMargin(this, left, top, right, bottom)

fun View.setMarginDpExt(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) = ViewUtils.setMargin(this, if (left == null) left else context.dp2px(left).toInt(), if (top == null) top else context.dp2px(top).toInt(), if (right == null) right else context.dp2px(right).toInt(), if (bottom == null) bottom else context.dp2px(bottom).toInt())

fun View.setPaddingExt(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) = ViewUtils.setPadding(this, left, top, right, bottom)

fun View.setWidthHeightExt(width: Int? = null, height: Int? = null) = ViewUtils.setWidthHeight(this, width)

fun View.getActivityExt() = ActivityUtils.getActivityByView(this)

//view转成图片
fun View.toBitmapExt(): Bitmap = ViewUtils.toBitmap(this)

//TabLayout
fun TabLayout.setIndicatorExt(leftDp: Int, rightDp: Int) = ViewUtils.setIndicator(this, leftDp, rightDp)

fun RecyclerView.addScrollPauseLoadExt() = RecyclerViewUtils.addScrollPauseLoad(this)

fun Dialog.showExt() = DialogUtil.safeShowDialog(this)
fun Dialog.dismissExt() = DialogUtil.safeDismissDialog(this)

fun TextView.textExt() = this.text.trim().toString()