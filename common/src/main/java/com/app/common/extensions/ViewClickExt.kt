package com.app.common.extensions

import android.view.View
import com.app.common.R

/**
 * Created by wr
 * Date: 2019/1/21  21:01
 * mail: 1902065822@qq.com
 * describe:
 * 防止快速点击
 */
/***
 * 防止快速点击
 * @param time Long 间隔，默认500毫秒
 */
fun <T : View> T.setOnClickExtNoFast(time: Long = 500L, block: (view: View) -> Unit) {
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