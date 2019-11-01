package com.app.common.widget.round

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.app.common.widget.round.delegate.RoundViewDelegate

/**
 * 自定义控件：圆角FrameLayout
 */
class RoundFrameLayoutOld : FrameLayout {
    private lateinit var mRoundViewDelegate: RoundViewDelegate

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initValue(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initValue(attrs)
    }

    private fun initValue(attrs: AttributeSet?) {
        mRoundViewDelegate = RoundViewDelegate(this, context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRoundViewDelegate.isWidthHeightEqual && width > 0 && height > 0) {
            val max = Math.max(width, height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mRoundViewDelegate.isRadiusHalfHeight) {
            mRoundViewDelegate.radius = height / 2f
        } else {
            mRoundViewDelegate.resetView();
        }
    }

    /** use delegate to set attr  */
    fun getDelegate(): RoundViewDelegate {
        return mRoundViewDelegate
    }

}
