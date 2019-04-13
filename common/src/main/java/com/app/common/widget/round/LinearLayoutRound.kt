package com.app.common.widget.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.app.common.widget.round.util.RoundViewDelegate

/**
 * 自定义控件：圆角RelativeLayout
 */
class LinearLayoutRound : LinearLayout {
    private var mRoundViewDelegate: RoundViewDelegate? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initValue(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initValue(attrs)
    }

    private fun initValue(attrs: AttributeSet?) {
        mRoundViewDelegate = RoundViewDelegate(this, context,attrs)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
                          bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val w = width
        val h = height
        mRoundViewDelegate?.roundRectSet(w, h)
    }

    override fun draw(canvas: Canvas) {
        mRoundViewDelegate?.canvasSetLayer(canvas)
        super.draw(canvas)
        canvas.restore()
    }

}
