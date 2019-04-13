package com.app.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnItemTouchListener
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * mRecyclerView 添加item 点击和长按事件
 * mRecyclerView.addOnItemTouchListener
 * Created by as on 2017/3/16.
 */

class RecyclerItemClickListener// 点击回调
(context: Context, recyclerView: RecyclerView?, private val mListener: OnItemClickListener?) : OnItemTouchListener {
    private val mGestureDetector: GestureDetector

    init {
        // 识别并处理手势
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                // 轻击触摸屏后,弹起，必须返回true，否则无法触发单击
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                if (recyclerView == null || e == null) {
                    return
                }
                // 长按根据findChildViewUnder(float x, float y)来算出哪个item被选择了
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                // 有item被选则且监听器不为空触发长按事件
                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView,
                            recyclerView.getChildLayoutPosition(childView))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView?, e: MotionEvent?): Boolean {
        if (view == null || e == null) {
            return false
        }
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            // 触发单击事件
            mListener.onItemClick(childView, view.getChildLayoutPosition(childView))
            return false//return true;
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }

}
