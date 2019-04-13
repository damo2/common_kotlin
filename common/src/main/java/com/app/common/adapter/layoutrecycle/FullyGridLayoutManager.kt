package com.app.common.adapter.layoutrecycle

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * ScrollView可以显示RecyclerView
 */
class FullyGridLayoutManager : GridLayoutManager {
    private val mMeasuredDimension = IntArray(2)
    var isScrollEnabled = true

    constructor(context: Context, spanCount: Int, orientation: Int = LinearLayoutManager.VERTICAL, reverseLayout: Boolean = false) : super(context, spanCount, orientation, reverseLayout) {}

    override fun getItemCount(): Int {
        return super.getItemCount()
    }


    // Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
    override fun canScrollVertically(): Boolean = isScrollEnabled && super.canScrollVertically()


    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)
        var width = 0
        var height = 0
        val count = itemCount
        val span = spanCount
        for (i in 0 until count) {
            measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension)
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (i % span == 0) {
                    width = width + mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height = height + mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        }
        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(recycler: RecyclerView.Recycler, position: Int, widthSpec: Int, heightSpec: Int, measuredDimension: IntArray) {
        if (position < itemCount) {
            try {
                val view = recycler.getViewForPosition(0)// fix 动态添加时报IndexOutOfBoundsException
                if (view != null) {
                    val p = view.layoutParams as RecyclerView.LayoutParams
                    val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, paddingLeft + paddingRight, p.width)
                    val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, paddingTop + paddingBottom, p.height)
                    view.measure(childWidthSpec, childHeightSpec)
                    measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
                    measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }

        }
    }
}
