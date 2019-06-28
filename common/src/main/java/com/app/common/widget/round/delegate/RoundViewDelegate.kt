package com.app.common.widget.round.delegate

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.app.common.R
import com.app.common.extensions.dp2px


/**
 * 圆角View 代理
 * 如果想改为圆形view，修改mRadiu为很大的数字，比如500
 * https://github.com/H07000223/FlycoRoundView
 */
class RoundViewDelegate(private val mView: View, private val mContext: Context, var attrs: AttributeSet?) {
    //默认圆角大小
    private val RadiusDefault = 5f

    var radius: Float = 0f
        set(value) {
            field = value
            setBgSelector()
        }
    var radiusTopLeft = 0f
        set(value) {
            field = value
            setBgSelector()
        }
    var radiusTopRight = 0f
        set(value) {
            field = value
            setBgSelector()
        }
    var radiusBottomLeft = 0f
        set(value) {
            field = value
            setBgSelector()
        }
    var radiusBottomRight = 0f
        set(value) {
            field = value
            setBgSelector()
        }
    var borderColor = 0
        set(value) {
            field = value
            setBgSelector()
        }
    var borderPressColor = 0
        set(value) {
            field = value
            setBgSelector()
        }
    var borderSize = 0
        set(value) {
            field = value
            setBgSelector()
        }
    //圆角为高的一半，圆形view
    var isRadiusHalfHeight: Boolean = false
        set(value) {
            field = value
            setBgSelector()
        }
    //宽高相等，以长边为准
    var isWidthHeightEqual: Boolean = false
        set(value) {
            field = value
            setBgSelector()
        }

    //颜色
    var backgroundColor: Int = 0
        set(value) {
            field = value
            setBgSelector()
        }
    var backgroundPressColor: Int = 0
        set(value) {
            field = value
            setBgSelector()
        }
    var textPressColor: Int = 0
        set(value) {
            field = value
            setBgSelector()
        }
    //是否水波纹
    var isRippleEnable: Boolean = false
        set(value) {
            field = value
            setBgSelector()
        }


    private val radiusArr = FloatArray(8)
    private val gdBackground = GradientDrawable()
    private val gdBackgroundPress = GradientDrawable()

    init {
        initAttr()
    }

    private fun initAttr() {
        attrs?.let {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.RoundView)
            radius = mContext.dp2px(a.getDimension(R.styleable.RoundView_radius, RadiusDefault))
            radiusTopLeft = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiusTopLeft, 0f))
            radiusTopRight = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiusTopRight, 0f))
            radiusBottomLeft = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiusBottomLeft, 0f))
            radiusBottomRight = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiusBottomRight, 0f))
            borderColor = a.getColor(R.styleable.RoundView_borderColor, 0)
            borderPressColor = a.getColor(R.styleable.RoundView_borderPressColor, 0)
            borderSize = mContext.dp2px(a.getDimension(R.styleable.RoundView_borderSize, 0f)).toInt()
            //背景颜色
            backgroundColor = a.getColor(R.styleable.RoundView_backgroundColor, 0)
            backgroundPressColor = a.getColor(R.styleable.RoundView_backgroundPressColor, 0)
            //文字颜色
            textPressColor = a.getColor(R.styleable.RoundView_textPressColor, 0)
            //是否水波纹
            isRippleEnable = a.getBoolean(R.styleable.RoundView_isRippleEnable, true)
            a.recycle()
        }
    }


    fun setBgSelector() {
        val bg = StateListDrawable()
        if (backgroundColor == 0) {
            (mView.background as? ColorDrawable)?.color?.let {
                backgroundColor = it
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
            setDrawable(gdBackground, backgroundColor, borderColor)
            val rippleDrawable = RippleDrawable(
                    getPressedColorSelector(backgroundColor, backgroundPressColor), gdBackground, null)
            mView.background = rippleDrawable
        } else {
            setDrawable(gdBackground, backgroundColor, borderColor)
            bg.addState(intArrayOf(-android.R.attr.state_pressed), gdBackground)
            if (backgroundPressColor != Integer.MAX_VALUE || borderPressColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundPress, if (backgroundPressColor == Integer.MAX_VALUE) backgroundColor else backgroundPressColor,
                        if (borderPressColor == Integer.MAX_VALUE) borderColor else borderPressColor)
                bg.addState(intArrayOf(android.R.attr.state_pressed), gdBackgroundPress)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                mView.background = bg
            } else {
                mView.setBackgroundDrawable(bg)
            }
        }

        if (mView is TextView || mView is Button) {
            if (textPressColor != Integer.MAX_VALUE) {
                val textColors = (mView as TextView).textColors
                //              Log.d("AAA", textColors.getColorForState(new int[]{-android.R.attr.state_pressed}, -1) + "");
                val colorStateList = ColorStateList(
                        arrayOf(intArrayOf(-android.R.attr.state_pressed), intArrayOf(android.R.attr.state_pressed)),
                        intArrayOf(textColors.defaultColor, textPressColor))
                mView.setTextColor(colorStateList)
            }
        }
    }

    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)

        if (radiusTopLeft > 0 || radiusTopRight > 0 || radiusBottomLeft > 0 || radiusBottomRight > 0) {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left */
            radiusArr[0] = radiusTopLeft.toFloat()
            radiusArr[1] = radiusTopLeft.toFloat()
            radiusArr[2] = radiusTopRight.toFloat()
            radiusArr[3] = radiusTopRight.toFloat()
            radiusArr[4] = radiusBottomRight.toFloat()
            radiusArr[5] = radiusBottomRight.toFloat()
            radiusArr[6] = radiusBottomLeft.toFloat()
            radiusArr[7] = radiusBottomLeft.toFloat()
            gd.cornerRadii = radiusArr
        } else {
            gd.cornerRadius = radius.toFloat()
        }

        gd.setStroke(borderSize.toInt(), strokeColor)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_focused), intArrayOf(android.R.attr.state_activated), intArrayOf()),
                intArrayOf(pressedColor, pressedColor, pressedColor, normalColor)
        )
    }

}
