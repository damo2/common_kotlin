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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.app.common.R

/**
 * 圆角ImageView
 *
 * @author venshine
 */
class RoundImageView : AppCompatImageView {

    //边框颜色
    var borderColor: Int = 0

    //边框宽度
    var borderWidth: Int = 0

    //是否圆形，默认如果图片宽高不相等即为椭圆
    var isCircle = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    //初始化属性
    private fun initAttrs(context: Context, attrs: AttributeSet) {
        try {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
            borderColor = ta.getColor(R.styleable.RoundImageView_borderColor, 0x00000000)
            borderWidth = ta.getDimension(R.styleable.RoundImageView_borderSize, 0f).toInt()
            isCircle = ta.getBoolean(R.styleable.RoundImageView_isCircle, true)
            ta.recycle()
            val resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1)
            if (resId != -1) {
                setImageResource(resId, borderColor, borderWidth, isCircle)
            }
        } catch (e: Error) {
            e.printStackTrace()
        }

    }

    /**
     * 设置边框颜色，形如'#aarrggbb'
     *
     * @param borderColor
     */
    fun setBorderColor(borderColor: String) {
        this.borderColor = Color.parseColor(borderColor)
    }


    /**
     * 设置图片资源
     *
     * @param resId
     */
    override fun setImageResource(resId: Int) {
        setImageResource(resId, borderColor, borderWidth, isCircle)
    }

    /**
     * 设置图片资源，包括边框颜色、边框宽度、是否圆形处理
     *
     * @param resId
     * @param borderColor
     * @param borderWidth
     * @param isCircle
     */
    fun setImageResource(resId: Int, borderColor: Int, borderWidth: Int, isCircle: Boolean) {
        setImageDrawable(resources.getDrawable(resId), borderColor, borderWidth, isCircle)
    }

    /**
     * 设置图片Drawable
     *
     * @param drawable
     */
    override fun setImageDrawable(drawable: Drawable?) {
        setImageDrawable(drawable, borderColor, borderWidth, isCircle)
    }

    /**
     * 设置图片Drawable，包括边框颜色、边框宽度、是否圆形处理
     *
     * @param drawable
     * @param borderColor
     * @param borderWidth
     * @param isCircle
     */
    fun setImageDrawable(drawable: Drawable?, borderColor: Int, borderWidth: Int, isCircle: Boolean) {
        val bm = drawableToBitmap(drawable)
        setImageBitmap(bm, borderColor, borderWidth, isCircle)
    }

    /**
     * 设置图片bitmap
     *
     * @param bm
     */
    override fun setImageBitmap(bm: Bitmap) {
        setImageBitmap(bm, borderColor, borderWidth, isCircle)
    }

    /**
     * 设置图片bitmap，包括边框颜色、边框宽度、是否圆形处理
     *
     * @param bm
     * @param borderColor
     * @param borderWidth
     * @param isCircle
     */
    fun setImageBitmap(bm: Bitmap?, borderColor: Int, borderWidth: Int, isCircle: Boolean) {
        super.setImageDrawable(RoundDrawable(bm, borderColor, borderWidth, isCircle))
    }

    /**
     * drawable 转化 bitmap
     *
     * @param d
     * @return
     */
    private fun drawableToBitmap(d: Drawable?): Bitmap? {
        return if (d == null) null else (d as BitmapDrawable).bitmap
    }
}
