package com.app.common.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by wr
 * Date: 2019/11/12  14:01
 * mail: 1902065822@qq.com
 * describe:
 */
fun RecyclerView.addOnMoreListenerExt(onMoreCallback: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var lastVisibleItemPosition: Int = 0
        //滚动状态改变时
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //没有滑动时 在最下面
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == recyclerView.adapter?.itemCount) {
                onMoreCallback()
            }
        }

        //滑动
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager
            lastVisibleItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> -1
            }
        }
    })
}
