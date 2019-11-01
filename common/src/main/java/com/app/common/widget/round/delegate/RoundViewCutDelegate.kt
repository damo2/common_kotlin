package com.app.common.widget.round.delegate

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.app.common.R
import com.app.common.extensions.dp2px
import com.app.common.logger.logd


/**
 * 圆角View 代理
 */
class RoundViewCutDelegate(private val mView: View, private val mContext: Context, var attrs: AttributeSet?) {
    private var mPath = Path()
    var radius: Float = 0f
        set(value) {
            field = value
            mView.invalidate()
        }
    var radiusTopLeft = 0f
        set(value) {
            field = value
            mView.invalidate()
        }
    var radiusTopRight = 0f
        set(value) {
            field = value
            mView.invalidate()
        }
    var radiusBottomLeft = 0f
        set(value) {
            field = value
            mView.invalidate()
        }
    var radiusBottomRight = 0f
        set(value) {
            field = value
            mView.invalidate()
        }
    //颜色
    var backgroundColor: Int = 0
    var backgroundPressColor: Int = 0
    //是否水波纹
    var isRippleEnable: Boolean = true

    init {
        initAttr()
        mPath.fillType = Path.FillType.EVEN_ODD
    }

    private fun initAttr() {
        attrs?.let {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.RoundViewLayout)
            radius = a.getDimension(R.styleable.RoundView_radius, 0f)
            radiusTopLeft =a.getDimension(R.styleable.RoundView_radiusTopLeft, 0f)
            radiusTopRight =a.getDimension(R.styleable.RoundView_radiusTopRight, 0f)
            radiusBottomLeft = a.getDimension(R.styleable.RoundView_radiusBottomLeft, 0f)
            radiusBottomRight = a.getDimension(R.styleable.RoundView_radiusBottomRight, 0f)
            backgroundColor = a.getColor(R.styleable.RoundViewLayout_backgroundColor, 0)
            backgroundPressColor = a.getColor(R.styleable.RoundViewLayout_backgroundPressColor, 0)
            isRippleEnable = a.getBoolean(R.styleable.RoundViewLayout_isRippleEnable, true)
            a.recycle()
        }
//        if (mView.background == null) {
//            mView.setBackgroundColor(mContext.resources.getColor(R.color.common_transparent))
//        }
        if (backgroundColor == 0) {
            (mView.background as? ColorDrawable)?.color?.let {
                backgroundColor = it
            }
        }
    }


    fun setDrawChange(): Path {
        checkPathChanged()
        return mPath
    }

    fun setBackgroundSelector() {
        logd("resetView")
        mView.backgroundTintList = getPressedColorSelector(backgroundColor, backgroundPressColor)
    }

    private fun checkPathChanged() {
        val mWidth = mView.width
        val mHeight = mView.height
        mPath.reset()
        if (radius > 0) {
            mPath.addRoundRect(RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()), radius, radius, Path.Direction.CW)
        } else {
            mPath.addRoundRect(RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()),
                    floatArrayOf(radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft),
                    Path.Direction.CW)
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_focused), intArrayOf(android.R.attr.state_activated), intArrayOf()),
                intArrayOf(pressedColor, pressedColor, pressedColor, normalColor)
        )
    }
}
