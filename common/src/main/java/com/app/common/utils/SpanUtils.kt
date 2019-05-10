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

package com.app.common.utils

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder

import com.app.common.utils.span.AlignMiddleImageSpan
import com.app.common.utils.span.MarginImageSpan

/**
 * @author cginechen
 * @date 2016-10-12
 */

object SpanUtils {
    /**
     * 在text左边或者右边添加icon,
     * 默认TextView添加leftDrawable或rightDrawable不能适应TextView match_parent的情况
     *
     * @param left true 则在文字左边添加 icon，false 则在文字右边添加 icon
     * @param text 文字内容
     * @param icon 需要被添加的 icon
     * @return 返回带有 icon 的文字
     */

    @JvmOverloads
    fun generateSideIconText(left: Boolean, iconPadding: Int, text: CharSequence, icon: Drawable?, iconOffsetY: Int = 0): CharSequence {
        return generateHorIconText(text, if (left) iconPadding else 0, if (left) icon else null, if (left) 0 else iconPadding, if (left) null else icon, iconOffsetY)
    }

    @JvmOverloads
    fun generateHorIconText(text: CharSequence, leftPadding: Int, iconLeft: Drawable?, rightPadding: Int, iconRight: Drawable?, iconOffsetY: Int = 0): CharSequence {
        if (iconLeft == null && iconRight == null) {
            return text
        }
        val iconTag = "[icon]"
        val builder = SpannableStringBuilder()
        var start: Int
        var end: Int
        if (iconLeft != null) {
            iconLeft.setBounds(0, 0, iconLeft.intrinsicWidth, iconLeft.intrinsicHeight)
            start = 0
            builder.append(iconTag)
            end = builder.length

            val imageSpan = MarginImageSpan(iconLeft, AlignMiddleImageSpan.ALIGN_MIDDLE, 0, leftPadding, iconOffsetY)
            imageSpan.setAvoidSuperChangeFontMetrics(true)
            builder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        builder.append(text)
        if (iconRight != null) {
            iconRight.setBounds(0, 0, iconRight.intrinsicWidth, iconRight.intrinsicHeight)
            start = builder.length
            builder.append(iconTag)
            end = builder.length

            val imageSpan = MarginImageSpan(iconRight, AlignMiddleImageSpan.ALIGN_MIDDLE, rightPadding, 0, iconOffsetY)
            imageSpan.setAvoidSuperChangeFontMetrics(true)
            builder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        return builder
    }
}
