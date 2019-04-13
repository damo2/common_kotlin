package com.app.common.widget.text

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.TextView

/**
 * 可设置选中状态的TextView
 * Created by wr
 * Date: 2018/8/23  9:07
 * describe:
 */

class CheckableTextView : TextView, Checkable {

    internal var checked: Boolean = false

    constructor(context: Context) : super(context) {}


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }


    override fun isChecked(): Boolean {
        return checked
    }


    override fun setChecked(arg0: Boolean) {
        checked = arg0
        refreshDrawableState()
    }


    override fun toggle() {
        checked = !checked
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }

}
