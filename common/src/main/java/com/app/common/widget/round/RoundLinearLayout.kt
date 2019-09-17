package com.app.common.widget.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.app.common.widget.round.delegate.RoundViewCutDelegate
import com.app.common.widget.round.delegate.RoundViewDelegate

/**
 * 自定义控件：圆角LinearLayout
 */
class RoundLinearLayout : LinearLayout {
    private lateinit var delegate: RoundViewDelegate

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initValue(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initValue(attrs)
    }

    private fun initValue(attrs: AttributeSet) {

        delegate = RoundViewDelegate(this, context, attrs)
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()
        val path = delegate.getPathChanged()
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(saveCount)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        delegate.setBackgroundSelector();
    }

}
