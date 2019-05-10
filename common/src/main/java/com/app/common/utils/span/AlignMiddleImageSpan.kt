/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.common.utils.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * 支持垂直居中的ImageSpan
 *
 * @author cginechen
 * @date 2016-03-17
 */
open class AlignMiddleImageSpan
/**
 * @param d                 作为 span 的 Drawable
 * @param verticalAlignment 垂直对齐方式, 如果要垂直居中, 则使用 [.ALIGN_MIDDLE]
 */
(d: Drawable, verticalAlignment: Int) : ImageSpan(d, verticalAlignment) {

    /**
     * 规定这个Span占几个字的宽度
     */
    private var mFontWidthMultiple = -1f

    /**
     * 是否避免父类修改FontMetrics，如果为 false 则会走父类的逻辑, 会导致FontMetrics被更改
     */
    private var mAvoidSuperChangeFontMetrics = false

    private var mWidth: Int = 0

    /**
     * @param d                 作为 span 的 Drawable
     * @param verticalAlignment 垂直对齐方式, 如果要垂直居中, 则使用 [.ALIGN_MIDDLE]
     * @param fontWidthMultiple 设置这个Span占几个中文字的宽度, 当该值 > 0 时, span 的宽度为该值*一个中文字的宽度; 当该值 <= 0 时, span 的宽度由 [.mAvoidSuperChangeFontMetrics] 决定
     */
    constructor(d: Drawable, verticalAlignment: Int, fontWidthMultiple: Float) : this(d, verticalAlignment) {
        if (fontWidthMultiple >= 0) {
            mFontWidthMultiple = fontWidthMultiple
        }
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        if (mAvoidSuperChangeFontMetrics) {
            val d = drawable
            val rect = d.bounds
            mWidth = rect.right
        } else {
            mWidth = super.getSize(paint, text, start, end, fm)
        }
        if (mFontWidthMultiple > 0) {
            mWidth = (paint.measureText("子") * mFontWidthMultiple).toInt()
        }
        return mWidth
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int,
                      x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        if (mVerticalAlignment == ALIGN_MIDDLE) {
            val d = drawable
            canvas.save()

            //            // 注意如果这样实现会有问题：TextView 有 lineSpacing 时，这里 bottom 偏大，导致偏下
            //            int transY = bottom - d.getBounds().bottom; // 底对齐
            //            transY -= (paint.getFontMetricsInt().bottom - paint.getFontMetricsInt().top) / 2 - d.getBounds().bottom / 2; // 居中对齐
            //            canvas.translate(x, transY);
            //            d.draw(canvas);
            //            canvas.restore();

            val fontMetricsInt = paint.fontMetricsInt
            val fontTop = y + fontMetricsInt.top
            val fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top
            val iconHeight = d.bounds.bottom - d.bounds.top
            val iconTop = fontTop + (fontMetricsHeight - iconHeight) / 2
            canvas.translate(x, iconTop.toFloat())
            d.draw(canvas)
            canvas.restore()
        } else {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint)
        }
    }

    /**
     * 是否避免父类修改FontMetrics，如果为 false 则会走父类的逻辑, 会导致FontMetrics被更改
     */
    fun setAvoidSuperChangeFontMetrics(avoidSuperChangeFontMetrics: Boolean) {
        mAvoidSuperChangeFontMetrics = avoidSuperChangeFontMetrics
    }

    companion object {

        val ALIGN_MIDDLE = -100 // 不要和父类重复
    }
}
