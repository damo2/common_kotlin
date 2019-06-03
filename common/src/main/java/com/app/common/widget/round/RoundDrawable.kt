/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.common.widget.round

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * 圆角Drawable
 *
 * @author venshine
 */
class RoundDrawable @JvmOverloads constructor(bitmap: Bitmap?, borderColor: Int = BORDER_COLOR, borderWidth: Int = BORDER_WIDTH, isCircle: Boolean = false) : Drawable() {

    private val mPaint = Paint()
    private val mBorderPaint = Paint()

    private val mRectF = RectF()
    private val mBorderRectF = RectF()

    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    private var mBorderWidth = 0
    private var mIsCircle = false

    init {
        if (bitmap == null) {
            Log.e(TAG, "RoundDrawable: bitmap cannot be null.")
        } else {
            // bitmap
            mBitmapWidth = bitmap.width
            mBitmapHeight = bitmap.height
            mIsCircle = isCircle
            mBorderWidth = borderWidth
            var bm: Bitmap? = null
            if (mIsCircle) {
                bm = getSquareBitmap(bitmap)
            }

            // paint
            mPaint.isAntiAlias = true
            mPaint.isDither = true
            mPaint.isFilterBitmap = true
            val shader = BitmapShader(if (bm == null) bitmap else bm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mPaint.shader = shader

            // border paint
            mBorderPaint.isAntiAlias = true
            mBorderPaint.isDither = true
            mBorderPaint.style = Paint.Style.STROKE
            mBorderPaint.color = borderColor
            mBorderPaint.strokeWidth = borderWidth.toFloat()
        }
    }

    /**
     * 返回宽高相等的Bitmap
     *
     * @param bitmap
     * @return
     */
    private fun getSquareBitmap(bitmap: Bitmap): Bitmap {
        val bm: Bitmap
        if (mBitmapWidth > mBitmapHeight) {
            bm = Bitmap.createBitmap(bitmap, (mBitmapWidth - mBitmapHeight) / 2, 0, mBitmapHeight,
                    mBitmapHeight)
            mBitmapWidth = mBitmapHeight
        } else if (mBitmapWidth < mBitmapHeight) {
            bm = Bitmap.createBitmap(bitmap, 0, (mBitmapHeight - mBitmapWidth) / 2, mBitmapWidth, mBitmapWidth)
            mBitmapHeight = mBitmapWidth
        } else {
            bm = bitmap
        }
        return bm
    }

    override fun draw(canvas: Canvas) {
        if (mIsCircle) {
            canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mRectF.centerX(), mPaint)
            canvas.drawCircle(mBorderRectF.centerX(), mBorderRectF.centerY(), mBorderRectF.centerX() - mBorderWidth / 2.0f,
                    mBorderPaint)
        } else {
            canvas.drawOval(mRectF, mPaint)
            canvas.drawOval(mBorderRectF, mBorderPaint)
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRectF.set(bounds)
        bounds.inset(mBorderWidth / 2, mBorderWidth / 2)   // FIXME fine tuning, [bounds.inset(mBorderWidth,
        // mBorderWidth);]
        mBorderRectF.set(bounds)
    }

    override fun setAlpha(alpha: Int) {
        if (mPaint.alpha != alpha) {
            mPaint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return mBitmapWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mBitmapHeight
    }

    fun setAntiAlias(aa: Boolean) {
        mPaint.isAntiAlias = aa
        invalidateSelf()
    }

    override fun setFilterBitmap(filter: Boolean) {
        mPaint.isFilterBitmap = filter
        invalidateSelf()
    }

    override fun setDither(dither: Boolean) {
        mPaint.isDither = dither
        invalidateSelf()
    }

    companion object {
        private val TAG = "RoundDrawable"
        /**
         * 边框颜色
         */
        private val BORDER_COLOR = 0x00000000

        /**
         * 边框宽度
         */
        private val BORDER_WIDTH = 0
    }

}
