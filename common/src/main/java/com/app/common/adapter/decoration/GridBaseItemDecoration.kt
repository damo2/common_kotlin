package com.app.common.adapter.decoration

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * 分割线
 */
class GridBaseItemDecoration(context: Context, private val spanCount: Int, var spacingH: Int,var  spacingV: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left = spacingH - column * spacingH / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacingH / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacingV
            }
            outRect.bottom = spacingV // item bottom
        } else {
            outRect.left = column * spacingH / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spacingH - (column + 1) * spacingH / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacingV // item top
            }
        }
    }
}
