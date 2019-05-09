package com.app.common.extensions

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by wr
 * Date: 2018/8/30  13:04
 * describe:
 */
fun RecyclerView.addScrollPauseLoadExt(){
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> Glide.with(context).resumeRequests()
                RecyclerView.SCROLL_STATE_DRAGGING -> Glide.with(context).pauseRequests()
                RecyclerView.SCROLL_STATE_SETTLING -> Glide.with(context).resumeRequests()
            }
        }
    })
}