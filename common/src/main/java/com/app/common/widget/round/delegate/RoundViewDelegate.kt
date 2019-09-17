package com.app.common.widget.round.delegate

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Path
import android.graphics.RectF
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
    var radiusTopLeft = 0f
    var radiusTopRight = 0f
    var radiusBottomLeft = 0f
    var radiusBottomRight = 0f
    var borderColor = 0
    var borderPressColor = 0
    var borderSize = 0

    //圆角为高的一半，圆形view
    var isRadiusHalfHeight: Boolean = false
    //宽高相等，以长边为准
    var isWidthHeightEqual: Boolean = false

    //颜色
    var backgroundColor: Int = 0
    var backgroundPressColor: Int = 0

    //背景图片
    var background: Int = 0
    var backgroundPress: Int = 0
    var textPressColor: Int = 0
    //是否水波纹
    var isRippleEnable: Boolean = false


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
            background = a.getResourceId(R.styleable.RoundView_backgroundDrawable, 0)
            backgroundPress = a.getResourceId(R.styleable.RoundView_backgroundPress, 0)
            //文字颜色
            textPressColor = a.getColor(R.styleable.RoundView_textPressColor, 0)
            //是否水波纹
            isRippleEnable = a.getBoolean(R.styleable.RoundView_isRippleEnable, true)
            a.recycle()
        }

        val isRepeatBg = backgroundColor != 0 && background != 0
        val isRepeatBgPress = backgroundPressColor != 0 && backgroundPress != 0
        if (isRepeatBg || isRepeatBgPress) {
            throw Error("颜色和图片资源不能同时设置")
        }

        if (mView.background == null) {
//            mView.setBackgroundColor(mContext.resources.getColor(R.color.common_transparent))
        }
        if (backgroundColor == 0) {
            (mView.background as? ColorDrawable)?.color?.let {
                backgroundColor = it
            }
        }
        if (backgroundPressColor != 0 || borderPressColor != 0 || textPressColor != 0) {
            mView.isClickable = true
//            if (mView is TextView) {
//                mView.isFocusable = true
//                mView.isFocusableInTouchMode = true
//            }
        }
    }


    fun setBackgroundSelector() {
        if (backgroundColor != 0 || backgroundPressColor != 0) {
            val gdBackground = getDrawableByColor(backgroundColor, borderColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
                mView.background = RippleDrawable(getPressedColorSelector(backgroundColor, backgroundPressColor), gdBackground, null)
            } else {
                val gdBackgroundPress = getDrawableByColor(backgroundPressColor, borderPressColor)
                StateListDrawable().apply {
                    addState(intArrayOf(-android.R.attr.state_pressed), gdBackground)
                    addState(intArrayOf(android.R.attr.state_pressed), gdBackgroundPress)
                }.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                        mView.background = it
                    } else {
                        mView.setBackgroundDrawable(it)
                    }
                }

            }
        }

        if (background != 0 || backgroundPress != 0) {
            StateListDrawable().apply {
                if (background == 0) {
                    background = backgroundPress
                }
                if (backgroundPress == 0) {
                    backgroundPress = background
                }
                if (background != 0) {
                    addState(intArrayOf(-android.R.attr.state_pressed), mContext.getDrawable(background))
                }
                if (backgroundPress != 0) {
                    addState(intArrayOf(android.R.attr.state_pressed), mContext.getDrawable(backgroundPress))
                }
            }.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                    mView.background = it
                } else {
                    mView.setBackgroundDrawable(it)
                }
            }
        }


        if (mView is TextView || mView is Button) {
            if (textPressColor != Integer.MAX_VALUE && textPressColor != 0) {
                val textColors = (mView as TextView).textColors
                //              Log.d("AAA", textColors.getColorForState(new int[]{-android.R.attr.state_pressed}, -1) + "");
                val colorStateList = ColorStateList(
                        arrayOf(intArrayOf(-android.R.attr.state_pressed), intArrayOf(android.R.attr.state_pressed)),
                        intArrayOf(textColors.defaultColor, textPressColor))
                mView.setTextColor(colorStateList)
            }
        }
    }

    private fun getDrawableByColor(color: Int, strokeColor: Int): GradientDrawable {
        val gd = GradientDrawable()
        if (color != 0) {
            gd.setColor(color)
        }
        if (radiusTopLeft > 0 || radiusTopRight > 0 || radiusBottomLeft > 0 || radiusBottomRight > 0) {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left */
            gd.cornerRadii = floatArrayOf(radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft)
        } else {
            gd.cornerRadius = radius
        }

        gd.setStroke(borderSize, strokeColor)
        return gd
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_focused), intArrayOf(android.R.attr.state_activated), intArrayOf()),
                intArrayOf(pressedColor, pressedColor, pressedColor, normalColor)
        )
    }


    fun getPathChanged(): Path {
        val path = Path().apply {
            fillType = Path.FillType.EVEN_ODD
        }
        val mWidth = mView.width
        val mHeight = mView.height
        path.reset()
        if (radius > 0) {
            path.addRoundRect(RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()), radius, radius, Path.Direction.CW)
        } else {
            path.addRoundRect(RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat()),
                    floatArrayOf(radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft),
                    Path.Direction.CW)
        }
        return path
    }

}
