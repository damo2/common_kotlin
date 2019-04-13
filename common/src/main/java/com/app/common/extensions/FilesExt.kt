package com.app.common.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.app.common.file.FileMetaData
import com.app.common.file.FileType
import com.app.common.file.HexKt
import java.io.File
import java.io.IOException

/**
 * @description 文件操作扩展
 */

/**
 * copy文件
 */
fun File.copyTo(target: String, overwrite: Boolean = true): File {
    val file = File(target)
    return copyTo(file, overwrite)
}

/**
 * 向文件中追加文本
 */
fun File.append(text: String, create: Boolean = true) {
    if (!this.exists()) {
        if (create) {
            this.createNewFile()
        } else {
            throw IOException("no such file")
        }
    }
    this.appendText(text)
}

/**
 * 从上往下遍历文件夹下的文件
 */
fun File.filter(p: ((File) -> Boolean)?): Sequence<File> = if (p != null) this.walkTopDown().filter(p) else this.walkTopDown()


fun File.iterator(p: ((File) -> Boolean)?): List<File> {
    return if (p == null) {
        this.walkTopDown().asIterable().toList()
    } else {
        this.walkTopDown().asIterable().filter(p)
    }
}

fun File.header(): String? {
    if (!this.exists()) {
        return null
    }
    val bytes = ByteArray(4)
    this.inputStream().use { it.read(bytes) }
    return HexKt.bytesToHexString(bytes)
}

fun File.bitmap(): Bitmap? {
    return if (!this.exists()) null
    else {
        val bytes = this.readBytes()
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

/**
 * 根据路径判断文件是否存在
 */
fun String.exits(): Boolean {
    return if (this.isBlank()) false else File(this).exists()
}

/**
 * 获取文件类型
 */
val File.metadata: FileMetaData
    get() {
        val header = this.header()
        if (header.isNullOrEmpty()) {
            return FileMetaData.UNKNOWN
        }
        val metas = FileMetaData.values().filter { it.headers.contains(header) }
        return if (metas.isNotEmpty()) metas.first() else FileMetaData.UNKNOWN
    }

/**
 * 是否是图片
 */
val File.isImage: Boolean
    get() = this.metadata.type == FileType.IMAGE
/**
 * @description 获取文件的大小
 * 如果文件存在 返回文件大小, 否则返回 0
 */
val File.size: Long
    get() = if (this.exists() && this.isFile) this.length() else 0


fun String.filename(): String = this.substringAfterLast("/", "")

fun String.filenameExcludeExtension(): String = this.substringBeforeLast(".", "")

fun String.extension(): String = this.substringAfterLast(".", "")


fun File.getVideoInfoExt(videoInfo: (duration: Int, width: Int, height: Int) -> Unit) {
    val mmr = MediaMetadataRetriever()
    var duration = "0"
    var width = "0"
    var height = "0"
    if (!path.isNullOrBlank()) {
        mmr.setDataSource(path)
        try {
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) // 播放时长单位为毫秒
            width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)//宽
            height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)//高
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
    videoInfo(Integer.valueOf(duration), Integer.valueOf(width), Integer.valueOf(height))
}