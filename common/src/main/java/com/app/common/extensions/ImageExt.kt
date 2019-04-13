package com.app.common.extensions

import android.graphics.BitmapFactory

/**
 * Created by wr
 * Date: 2018/11/26  13:12
 * describe:
 */

fun String.imgGetWidthExt(): Int {
    try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(this, options);
        return options.outWidth
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

fun String.imgGetHeightExt(): Int {
    try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(this, options);
        return options.outHeight
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

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