package com.app.common.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.common.R
import com.app.common.extensions.getColorExt

/**
 * Created by wr
 * Date: 2019/9/25  16:22
 * mail: 1902065822@qq.com
 * describe:
 */
class CommItemDecoration(
        context: Context,
        private val mOrientation: Int = VERTICAL_LIST,
        @ColorInt color: Int = context.getColorExt(R.color.common_lineColor),
        private val space: Int = 1,
        private val left: Int = 0,
        private val isShowLast: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val mRect = Rect(0, 0, 0, 0)
    private val mPaint = Paint()

    init {
        mPaint.color = color
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + left
        val right = parent.width - parent.paddingRight

        //最后一个不显示 -1
        val childCount = parent.childCount - if (isShowLast) 0 else 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + space
            mRect.set(left, top, right, bottom)
            c.drawRect(mRect, mPaint)
        }
    }

    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + space
            mRect.set(left, top, right, bottom)
            c.drawRect(mRect, mPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, space)
        } else {
            outRect.set(0, 0, space, 0)
        }
    }

    companion object {
        private val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        private val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}

