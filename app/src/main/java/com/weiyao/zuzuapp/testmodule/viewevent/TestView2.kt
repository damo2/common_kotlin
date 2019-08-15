package com.weiyao.zuzuapp.testmodule.viewevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.LinearLayoutCompat
import com.app.common.logger.logd
import com.app.common.view.toastInfo

/**
 * Created by wr
 * Date: 2019/8/14  13:29
 * mail: 1902065822@qq.com
 * describe:
 */
class TestView2 : LinearLayoutCompat {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    }

    init {
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        logd("onTouchEvent ${event?.action}")
        if (event?.action?.equals(MotionEvent.ACTION_DOWN) == true) {
            toastInfo("TestView2 点击")
            logd("TestView2 点击")
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        logd("dispatchTouchEvent ${ev?.action}")
        if (ev?.action?.equals(MotionEvent.ACTION_DOWN) == true) {
//            return false
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        logd("onInterceptTouchEvent ${ev?.action}")
        if (ev?.action?.equals(MotionEvent.ACTION_DOWN) == true) {

        }
        return false
    }
}