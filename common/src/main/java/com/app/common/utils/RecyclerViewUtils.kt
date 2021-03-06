package com.app.common.utils

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by wr
 * Date: 2019/5/10  15:50
 * mail: 1902065822@qq.com
 * describe:
 */
object RecyclerViewUtils {
    fun addScrollPauseLoad(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> Glide.with(recyclerView.context).resumeRequests()
                    RecyclerView.SCROLL_STATE_DRAGGING -> Glide.with(recyclerView.context).pauseRequests()
                    RecyclerView.SCROLL_STATE_SETTLING -> Glide.with(recyclerView.context).resumeRequests()
                }
            }
        })
    }
}