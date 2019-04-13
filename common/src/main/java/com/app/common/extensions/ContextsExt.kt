package com.app.common.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import com.app.common.utils.StorageUtils
import java.io.File


/**
 *  getUri
 */
fun Context.getUriFromFile(file: File): Uri {
    val imageUri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
    } else {
        imageUri = Uri.fromFile(file)
    }
    return imageUri
}


/*
---------------------------     单位转换start        --------------------------------
 */
fun Context.dp2px(dpVal: Int): Int = dp2px(dpVal.toFloat()).toInt()

fun Context.dp2pxFI(dpVal: Float): Int = dp2px(dpVal).toInt()
fun Context.dp2px(dpVal: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, this.resources.displayMetrics)

fun Context.sp2px(spVal: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, this.resources.displayMetrics)
/*
---------------------------     单位转换end        --------------------------------
 */

/*
---------------------------     文件操作start        --------------------------------
 */
fun Context.writeBytesToCacheDir(bytes: ByteArray, filename: String) {
    if (StorageUtils.isExternalStorageWritable()) {
        val dic = this.cacheDir
        if (dic != null) {
            val file = File(dic, filename)
            file.writeBytes(bytes)
        }
    }
}

fun Context.writeBytesToExternalCacheDir(bytes: ByteArray, filename: String) {
    if (StorageUtils.isExternalStorageWritable()) {
        val dic = this.externalCacheDir
        if (dic != null) {
            val file = File(dic, filename)
            file.writeBytes(bytes)
        }
    }
}

fun Context.writeBitmapToToExternalCacheDir(bitmap: Bitmap, filename: String) {
    if (StorageUtils.isExternalStorageWritable()) {
        val dic = this.externalCacheDir
        if (dic != null) {
            val stream = File(dic, filename).outputStream()
            stream.use {
                if (filename.extension().toUpperCase() == "PNG") {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
            }
        }
    }
}

fun Context.getExternalCachePath(childPath: String): String? {
    var path: String? = null
    if (StorageUtils.isExternalStorageWritable()) {
        path = this.externalCacheDir?.absolutePath
    }
    if (path == null) {
        path = this.cacheDir?.absolutePath
    }
    return if (path != null) {
        val file = File("""$path${File.separator}$childPath""")
        file.mkdirs()
        file.absolutePath
    } else {
        null
    }
}
/*
---------------------------     文件操作end        --------------------------------
 */


/*
---------------------------     获取资源start        --------------------------------
 */
fun Context.getDrawableExt(@DrawableRes id: Int, @Nullable theme: Resources.Theme?)
        = ResourcesCompat.getDrawable(resources, id, theme)

fun Context.getDrawableExt(@DrawableRes id: Int)
        = ContextCompat.getDrawable(this, id)

fun Context.getColorExt(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getColorExt(@ColorRes id: Int, theme: Resources.Theme?) =
        ResourcesCompat.getColor(this.resources, id, theme)

//getColorStateList过时方法处理
fun Context.getColorStateListExt(@ColorRes id: Int): ColorStateList =
        ContextCompat.getColorStateList(this, id)

//getColorStateList过时方法处理
fun Context.getColorStateListExtt(@ColorRes id: Int, theme: Resources.Theme?): ColorStateList? {
    return ResourcesCompat.getColorStateList(this.resources, id, theme)
}
/*
---------------------------     获取资源end        --------------------------------
 */