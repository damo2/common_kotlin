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
import android.graphics.drawable.Drawable

/**
 * 支持设置图片左右间距的 ImageSpan
 *
 * @author chantchen
 * @date 2015-12-16
 */
class MarginImageSpan(d: Drawable, verticalAlignment: Int, marginLeft: Int, marginRight: Int) : AlignMiddleImageSpan(d, verticalAlignment) {

    private var mSpanMarginLeft = 0
    private var mSpanMarginRight = 0
    private var mOffsetY = 0

    init {
        mSpanMarginLeft = marginLeft
        mSpanMarginRight = marginRight
    }

    constructor(d: Drawable, verticalAlignment: Int, marginLeft: Int, marginRight: Int, offsetY: Int) : this(d, verticalAlignment, marginLeft, marginRight) {
        mOffsetY = offsetY
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        if (mSpanMarginLeft != 0 || mSpanMarginRight != 0) {
            super.getSize(paint, text, start, end, fm)
            val d = drawable
            return d.intrinsicWidth + mSpanMarginLeft + mSpanMarginRight
        } else {
            return super.getSize(paint, text, start, end, fm)
        }
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int,
                      x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        canvas.save()
        canvas.translate(0f, mOffsetY.toFloat())
        // marginRight不用专门处理，只靠getSize()中改变即可
        super.draw(canvas, text, start, end, x + mSpanMarginLeft, top, y, bottom, paint)
        canvas.restore()
    }
}
