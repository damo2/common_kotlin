package com.app.common.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import com.app.common.utils.BitmapUtil
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by wr
 * Date: 2018/8/31  11:02
 * describe:
 */

//保存文件（需要判断权限）
fun Bitmap.saveFileExt(path: String) {
    val file = File(path)
    if (file.parentFile != null && file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    BufferedOutputStream(FileOutputStream(file)).use { bos ->
        compress(Bitmap.CompressFormat.JPEG, 100, bos)
    }
}

//从一个view创建Bitmap。
fun View.createBitmapFromViewExt(scale: Float)=BitmapUtil.createBitmapFromView(this,scale)

fun View.createBitmapFromView(): Bitmap? =createBitmapFromViewExt(1f)

//图片压缩
fun Bitmap.compressImage()=BitmapUtil.compressImage(this)

