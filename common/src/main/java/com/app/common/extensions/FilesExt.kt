package com.app.common.extensions

import android.graphics.Bitmap
import com.app.common.file.FileMetaData
import com.app.common.file.FileType
import com.app.common.utils.FileUtils
import java.io.File

/**
 * @description 文件操作扩展
 */

//获取文件头
val File.headerExt: String?
    get() = FileUtils.header(this)

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
val String.filenameExt: String
    get() = FileUtils.filename(this)

//文件名字不带后缀
val String.filenameNoExtensionExt: String
    get() = FileUtils.filenameNoExtension(this)

//文件后缀
val String.fileExtensionExt: String
    get() = FileUtils.fileExtension(this)

//获取视频信息
fun File.getVideoInfoExt(videoInfo: (duration: Int, width: Int, height: Int) -> Unit) = FileUtils.getVideoInfo(this, videoInfo)

//向文件中追加文本
fun File.appendExt(txt: String, isCreate: Boolean = true) = FileUtils.append(this, txt, isCreate)

fun File.bitmapExt(): Bitmap? = FileUtils.bitmap(this)

//拼接目录，最后"/"结尾
fun dirAppendExt(vararg str: String?) = FileUtils.dirAppend(str)