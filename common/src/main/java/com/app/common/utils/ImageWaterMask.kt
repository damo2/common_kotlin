package com.app.common.utils

import android.content.res.Resources
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by wr
 * Date: 2019/9/3  8:48
 * mail: 1902065822@qq.com
 * describe:
 * 图片添加水印
 */
object ImageWaterMask {
    /**
     * 给一张Bitmap添加水印文字。
     *
     * @param bitmap      源图片
     * @param content  水印文本
     * @param textSize 水印字体大小 ，单位pix。
     * @param color    水印字体颜色。
     * @param x        起始坐标x
     * @param y        起始坐标y
     * @param positionFlag  居左/居右
     * @param recycle  是否回收
     * @return 已经添加水印后的Bitmap。
     */
    fun addTextWatermark(bitmap: Bitmap, content: String?, textSize: Int, @ColorInt color: Int = Color.BLACK, x: Float, y: Float, positionFlag: Boolean, recycle: Boolean): Bitmap? {
        if (isEmptyBitmap(bitmap) || content == null) {
            return null
        }
        val ret = bitmap.copy(bitmap.config, true)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(ret)
        paint.color = color
        paint.textSize = textSize.toFloat()
        val bounds = Rect()
        paint.getTextBounds(content, 0, content.length, bounds)
        canvas.drawText(content, bitmap.width.toFloat() - x - bounds.width().toFloat() - bounds.left.toFloat(), bitmap.height.toFloat() - bounds.height().toFloat() - bounds.top.toFloat() - y, paint)

        if (positionFlag) {
            canvas.drawText(content, x, bitmap.height.toFloat() - bounds.height().toFloat() - bounds.top.toFloat() - y, paint)
        } else {
            canvas.drawText(content, bitmap.width.toFloat() - x - bounds.width().toFloat() - bounds.left.toFloat(), bitmap.height.toFloat() - bounds.height().toFloat() - bounds.top.toFloat() - y, paint)
        }
        if (recycle && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        return ret
    }

    /**
     * Bitmap对象是否为空。
     */
    fun isEmptyBitmap(src: Bitmap?): Boolean {
        return src == null || src.width == 0 || src.height == 0
    }

    /**
     * 设置水印图片在左上角
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    fun createWaterMaskLeftTop(src: Bitmap, watermark: Bitmap, paddingLeft: Int, paddingTop: Int): Bitmap? {
        return createWaterMaskBitmap(src, watermark, dp2px(paddingLeft.toFloat()), dp2px(paddingTop.toFloat()))
    }

    /**
     * 设置水印图片在右下角
     */
    fun createWaterMaskRightBottom(src: Bitmap, watermark: Bitmap, paddingRight: Int, paddingBottom: Int): Bitmap? {
        return createWaterMaskBitmap(src, watermark, src.width - watermark.width - dp2px(paddingRight.toFloat()), src.height - watermark.height - dp2px(paddingBottom.toFloat()))
    }

    /**
     * 设置水印图片到右上角
     */
    fun createWaterMaskRightTop(src: Bitmap, watermark: Bitmap, paddingRight: Int, paddingTop: Int): Bitmap? {
        return createWaterMaskBitmap(src, watermark, src.width - watermark.width - dp2px(paddingRight.toFloat()), dp2px(paddingTop.toFloat()))
    }

    /**
     * 设置水印图片到左下角
     */
    fun createWaterMaskLeftBottom(src: Bitmap, watermark: Bitmap, paddingLeft: Int, paddingBottom: Int): Bitmap? {
        return createWaterMaskBitmap(src, watermark, dp2px(paddingLeft.toFloat()), src.height - watermark.height - dp2px(paddingBottom.toFloat()))
    }

    /**
     * 设置水印图片到中间
     */
    fun createWaterMaskCenter(src: Bitmap, watermark: Bitmap): Bitmap? {
        return createWaterMaskBitmap(src, watermark, (src.width - watermark.width) / 2, (src.height - watermark.height) / 2)
    }

    fun createWaterMaskBitmap(src: Bitmap?, watermark: Bitmap, paddingLeft: Int, paddingTop: Int): Bitmap? {
        if (src == null) {
            return null
        }
        val width = src.width
        val height = src.height
        //创建一个bitmap
        val newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        val canvas = Canvas(newb)
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0f, 0f, null)
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft.toFloat(), paddingTop.toFloat(), null)
        // 保存 canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save()
        // 存储
        canvas.restore()
        return newb
    }

    /**
     * 将View转成Bitmap
     */
    fun viewToBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        return view.drawingCache
    }

    /**
     * dp转pix
     *
     * @param dp
     * @return
     */
    fun dp2px(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }


    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    fun save(src: Bitmap, file: File, format: Bitmap.CompressFormat, recycle: Boolean): Boolean {
        if (isEmptyBitmap(src)) {
            return false
        }
        val os: OutputStream
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = src.compress(format, 100, os)
            if (recycle && !src.isRecycled) {
                src.recycle()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ret
    }

}
