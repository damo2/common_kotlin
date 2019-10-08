package com.app.common.extensions

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.RequiresPermission
import com.app.common.utils.BitmapUtil
import com.app.common.utils.CompressConst

/**
 * Created by wr
 * Date: 2018/8/31  11:02
 * describe:
 */

//保存文件（需要判断权限）
//@RequiresPermission(allOf = [(Manifest.permission.READ_EXTERNAL_STORAGE), (Manifest.permission.WRITE_EXTERNAL_STORAGE)])
fun Bitmap.saveFileExt(path: String, isRecycle: Boolean, sucCallback: (() -> Unit)? = null) = BitmapUtil.saveFile(this, path, isRecycle, sucCallback)

//从一个view创建Bitmap。
fun View.createBitmapFromViewExt(scale: Float) = BitmapUtil.createBitmapFromView(this, scale)

fun View.createBitmapFromViewExt(): Bitmap? = createBitmapFromViewExt(1f)

fun View.createBitmapFromViewExt(leftCrop: Int, topCrop: Int, rightCrop: Int, bottomCrop: Int) = BitmapUtil.createBitmapFromView(this, leftCrop, topCrop, rightCrop, bottomCrop)

//图片压缩
fun Bitmap.compressImageExt(sizeMax: Int = CompressConst.IMAGE_MAXSIZE_SYS) = BitmapUtil.compressImage(this, sizeMax)

fun String.compressByPxExt() = BitmapUtil.compressByPx(this)

fun Bitmap.compressBySizeExt(sizeMax: Int = CompressConst.IMAGE_MAXSIZE_SYS) = BitmapUtil.compressBySize(this, sizeMax)

fun Drawable?.toBitmapExt() = BitmapUtil.drawableToBitmap(this)

fun Uri.toBitmapExt(context: Context) = BitmapUtil.uriToBitmap(context, this)

//图片路径获取宽
fun String.imgGetWidthExt() = BitmapUtil.imgGetWidth(this)

//图片路径获取高
fun String.imgGetHeightExt() = BitmapUtil.imgGetHeight(this)



