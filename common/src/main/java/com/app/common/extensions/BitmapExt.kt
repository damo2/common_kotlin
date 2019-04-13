package com.app.common.extensions

import android.graphics.Bitmap
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