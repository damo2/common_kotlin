package com.app.common.extensions

import android.graphics.Bitmap
import com.app.common.file.FileMetaData
import com.app.common.file.FileType
import com.app.common.utils.FileUtils
import java.io.File

/**
 * @description 文件操作扩展
 */

//向文件中追加文本
fun File.appendExt(txt: String, create: Boolean = true) = FileUtils.append(this, txt, create)

fun File.headerExt(): String? = FileUtils.header(this)

//获取文件类型
val File.metadataExt: FileMetaData
    get() = FileUtils.metadata(this)

//是否是图片
val File.isImageExt: Boolean
    get() = this.metadataExt.type == FileType.IMAGE

//获取文件的大小
val File.sizeExt: Long
    get() = FileUtils.size(this)

//文件名字带后缀
fun String.filenameExt(): String = FileUtils.filename(this)

//文件名字不带后缀
fun String.filenameNoExtensionExt(): String = FileUtils.filenameNoExtension(this)

//文件后缀
fun String.fileExtensionExt(): String = FileUtils.fileExtension(this)

//获取视频信息
fun File.getVideoInfoExt(videoInfo: (duration: Int, width: Int, height: Int) -> Unit) = FileUtils.getVideoInfo(this, videoInfo)

fun File.bitmapExt(): Bitmap? = FileUtils.bitmap(this)