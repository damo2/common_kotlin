package com.app.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.app.common.utils.BitmapUtil

/**
 * Created by wr
 * Date: 2018/8/31  11:02
 * describe:
 */

//保存文件（需要判断权限）
fun Bitmap.saveFileExt(path: String, sucCallback: (() -> Unit)? = null) = BitmapUtil.saveFile(this, path, sucCallback)

//从一个view创建Bitmap。
fun View.createBitmapFromViewExt(scale: Float) = BitmapUtil.createBitmapFromView(this, scale)

fun View.createBitmapFromViewExt(): Bitmap? = createBitmapFromViewExt(1f)

fun View.createBitmapFromViewExt(leftCrop: Int, topCrop: Int, rightCrop: Int, bottomCrop: Int) = BitmapUtil.createBitmapFromView(this, leftCrop, topCrop, rightCrop, bottomCrop)

//图片压缩
fun Bitmap.compressImageExt() = BitmapUtil.compressImage(this)

fun String.compressByPxExt() = BitmapUtil.compressByPx(this)

fun Bitmap.compressBySizeExt() = BitmapUtil.compressBySize(this)

fun Drawable.drawableToBitmapExt() = BitmapUtil.drawableToBitmap(this)

fun Uri.toBitmapExt(context: Context) = BitmapUtil.uriToBitmap(context, this)



