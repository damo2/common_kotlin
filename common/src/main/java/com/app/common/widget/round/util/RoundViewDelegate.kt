package com.app.common.widget.round.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.View
import com.app.common.R
import com.app.common.extensions.dp2px


/**
 * 圆角View 代理
 * 如果想改为圆形view，修改mRadiu为很大的数字，比如10000
 */
class RoundViewDelegate(private val mView: View?, private val mContext: Context, var attrs: AttributeSet?) {
    private val roundRect = RectF()
    private val maskPaint = Paint()
    private val zonePaint = Paint()
    private var mRadius: Float = 0f
    private var mRadiuTopLeft = 0f
    private var mRadiuTopRight = 0f
    private var mRadiuBottomLeft = 0f
    private var mRadiuBottomRight = 0f
    private var mBorderColor = 0
    private var mBorderSize = 0f
    //默认圆角大小
    private val RadiusDefault = 5f

    init {
        init()
    }

    private fun init() {
        maskPaint.isAntiAlias = true
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //
        zonePaint.isAntiAlias = true
        zonePaint.color = Color.WHITE
        attrs?.let {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.RoundView)
            mRadius = mContext.dp2px(a.getDimension(R.styleable.RoundView_radius, RadiusDefault))
            mRadiuTopLeft = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiuTopLeft, 0f))
            mRadiuTopRight = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiuTopRight, 0f))
            mRadiuBottomLeft = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiuBottomLeft, 0f))
            mRadiuBottomRight = mContext.dp2px(a.getDimension(R.styleable.RoundView_radiuBottomRight, 0f))
            mBorderColor = a.getColor(R.styleable.RoundView_borderColor, 0)
            mBorderSize = mContext.dp2px(a.getDimension(R.styleable.RoundView_borderSize, 0f))
            a.recycle()
        }
    }

    /**
     * 从新设置圆角
     *
     * @param radiu
     */
    fun setRadius(radiu: Float) {
        mRadius = radiu
        mView?.invalidate()
    }

    fun setRadiuTopLeft(radiuTopLeft: Float) {
        mRadiuTopLeft = radiuTopLeft
        mView?.invalidate()
    }

    fun setRadiuTopRight(radiuTopRight: Float) {
        mRadiuTopRight = radiuTopRight
        mView?.invalidate()
    }


    fun setRadiuBottomLeft(radiuBottomLeft: Float) {
        mRadiuBottomLeft = radiuBottomLeft
        mView?.invalidate()
    }


    fun setRadiuBottomRight(radiuBottomRight: Float) {
        mRadiuBottomRight = radiuBottomRight
        mView?.invalidate()
    }

    /**
     * 圆角区域设置
     *
     * @param width
     * @param height
     */
    fun roundRectSet(width: Int, height: Int) {
        roundRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    /**
     * 画布区域裁剪
     *
     * @param canvas
     */
    fun canvasSetLayer(canvas: Canvas) {
        if (mRadius > 0) {
            val min = Math.min(roundRect.width(), roundRect.height())
            if (mRadius > min / 2) {
                mRadius = min / 2
            }
            canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG)
            canvas.drawRoundRect(roundRect, mRadius, mRadius, zonePaint)
            canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG)
        }
        val isRadiuOne = mRadiuTopLeft > 0 || mRadiuTopRight > 0 || mRadiuBottomLeft > 0 || mRadiuBottomRight > 0
        if (isRadiuOne) {
            // 1,2 左上角;3,4 右上角;5,6右下;7,8 左下;如果没弧度的话，传入null即可。
            // 外部矩形弧度
            val outerR = floatArrayOf(mRadiuTopLeft, mRadiuTopLeft, mRadiuTopRight, mRadiuTopRight, mRadiuBottomLeft, mRadiuBottomLeft, mRadiuBottomRight, mRadiuBottomRight)

            // 内部矩形与外部矩形的距离
            val inset = if (mBorderSize > 0) RectF(mBorderSize, mBorderSize, mBorderSize, mBorderSize) else null
            // 内部矩形弧度
            val innerR = if (mBorderSize > 0) floatArrayOf(mRadiuTopLeft, mRadiuTopLeft, mRadiuTopRight, mRadiuTopRight, mRadiuBottomLeft, mRadiuBottomLeft, mRadiuBottomRight, mRadiuBottomRight) else null
            val rr = RoundRectShape(outerR, inset, innerR)
            val drawable = ShapeDrawable(rr)
            //指定填充颜色
            drawable.paint.color = mBorderColor
            // 指定填充模式
            drawable.paint.style = Paint.Style.FILL
            mView?.setBackgroundDrawable(drawable)
        }
    }

}
