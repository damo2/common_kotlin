package com.app.common.utils

import android.graphics.Color

import androidx.annotation.ColorInt

/**
 * Created by wr
 * Date: 2019/5/8  17:29
 * mail: 1902065822@qq.com
 * describe:
 */
object ColorUtils {

    /**
     * 设置颜色的alpha值
     *
     * @param color    需要被设置的颜色值
     * @param alpha    取值为[0,1]，0表示全透明，1表示不透明
     * @param override 覆盖原本的 alpha
     * @return 返回改变了 alpha 值的颜色值
     */
    @JvmOverloads
    fun setColorAlpha(@ColorInt color: Int, alpha: Float, override: Boolean = true): Int {
        val origin = if (override) 0xff else color shr 24 and 0xff
        return color and 0x00ffffff or ((alpha * origin).toInt() shl 24)
    }

    /**
     * 根据比例，在两个color值之间计算出一个color值
     * **注意该方法是ARGB通道分开计算比例的**
     *
     * @param fromColor 开始的color值
     * @param toColor   最终的color值
     * @param fraction  比例，取值为[0,1]，为0时返回 fromColor， 为1时返回 toColor
     * @return 计算出的color值
     */
    fun computeColor(@ColorInt fromColor: Int, @ColorInt toColor: Int, fraction: Float): Int {
        var fraction = fraction
        fraction = LangUtils.constrain(fraction, 0f, 1f)

        val minColorA = Color.alpha(fromColor)
        val maxColorA = Color.alpha(toColor)
        val resultA = ((maxColorA - minColorA) * fraction).toInt() + minColorA

        val minColorR = Color.red(fromColor)
        val maxColorR = Color.red(toColor)
        val resultR = ((maxColorR - minColorR) * fraction).toInt() + minColorR

        val minColorG = Color.green(fromColor)
        val maxColorG = Color.green(toColor)
        val resultG = ((maxColorG - minColorG) * fraction).toInt() + minColorG

        val minColorB = Color.blue(fromColor)
        val maxColorB = Color.blue(toColor)
        val resultB = ((maxColorB - minColorB) * fraction).toInt() + minColorB

        return Color.argb(resultA, resultR, resultG, resultB)
    }

    /**
     * 将 color 颜色值转换为十六进制字符串
     *
     * @param color 颜色值
     * @return 转换后的字符串
     */
    fun colorToString(@ColorInt color: Int): String {
        return String.format("#%08X", color)
    }

}
