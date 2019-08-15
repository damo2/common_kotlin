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
class TestView3 : LinearLayoutCompat {
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
            toastInfo("TestView3 点击")
            logd("TestView3 点击")
//            return true//事件被消费
//            return false//执行父view 的onTouchEvent
        }
        return super.onTouchEvent(event)// 默认false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        logd("dispatchTouchEvent ${ev?.action}")
        if (ev?.action?.equals(MotionEvent.ACTION_DOWN) == true) {

        } else {

        }
//        return true //事件被消费
//        return false //返回false 执行父view的onTouchEvent。
        return super.dispatchTouchEvent(ev) //继续分发 执行onInterceptTouchEvent
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        logd("onInterceptTouchEvent ${ev?.action}")
        if (ev?.action?.equals(MotionEvent.ACTION_DOWN) == true) {
//            return true //返回 true 执行onTouchEvent
        }
        return false//返回 false 不拦截  执行子view的dispatchTouchEvent
        return super.onInterceptTouchEvent(ev) //默认 true
    }
}