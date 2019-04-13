package com.app.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileOutputStream

/**
 * Created by wr
 * Date: 2018/9/13  19:04
 * describe:
 */
object ImageUtils {
    //webp 格式转png
    fun webpToPng(webpPath: String,pngPath:String){
        FileOutputStream(pngPath).use { fos ->
            val bitmap = BitmapFactory.decodeFile(webpPath, null)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
        }
    }

    //图片自适应
    fun imgWrapExt(width: Int, height: Int, minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int, finalWidthHeightBack: (finalWidth: Int, finalHeight: Int) -> Unit) {
        val ratioWH = width.toFloat() / height.toFloat()
        val ratioWHMax = maxWidth.toFloat() / minHeight.toFloat()
        val ratioWHMin = minWidth.toFloat() / maxHeight.toFloat()
        val ratioWrap = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = width
        var finalHeight = height
        if (ratioWH > ratioWHMax) {
            finalWidth = maxWidth
            finalHeight = minHeight
        } else if (ratioWH < ratioWHMin) {
            finalWidth = minWidth
            finalHeight = maxHeight
        } else {
            if (ratioWH < ratioWrap) {
                if (height < minHeight) {
                    finalHeight = minHeight
                } else if (height > maxHeight) {
                    finalHeight = maxHeight
                } else {
                    finalHeight = height
                }
                finalWidth = (finalHeight * ratioWH).toInt()
            } else {
                if (width < minWidth) {
                    finalWidth = minWidth
                } else if (width > maxWidth) {
                    finalWidth = maxWidth
                } else {
                    finalWidth = width
                }
                finalHeight = (finalWidth / ratioWH).toInt()
            }
        }
        finalWidthHeightBack(finalWidth, finalHeight)
    }
}