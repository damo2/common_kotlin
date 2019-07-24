package com.app.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.app.common.extensions.headerExt
import com.app.common.file.FileMetaData
import com.app.common.file.HexKt
import java.io.File
import java.io.IOException

/**
 * Created by wr
 * Date: 2019/7/9  16:33
 * mail: 1902065822@qq.com
 * describe:
 */
object FileUtils {
    /**
     * 向文件中追加文本
     */
    fun append(file: File, txt: String, create: Boolean = true) {
        if (!file.exists()) {
            if (create) {
                file.createNewFile()
            } else {
                throw IOException("no such file")
            }
        }
        file.appendText(txt)
    }

    /**
     * 获取文件类型
     */
    fun metadata(file: File): FileMetaData {
        val header = header(file)
        if (header.isNullOrEmpty()) {
            return FileMetaData.UNKNOWN
        }
        return FileMetaData.values().asSequence().firstOrNull { it.headers.contains(header) }
                ?: FileMetaData.UNKNOWN
    }


    fun header(file: File): String? {
        if (!file.exists()) {
            return null
        }
        val bytes = ByteArray(4)
        file.inputStream().use { it.read(bytes) }
        return HexKt.bytesToHexString(bytes)
    }

    //文件大小
    fun size(file: File): Long = if (file.exists() && file.isFile) file.length() else 0

    //文件名字带后缀
    fun filename(filePath: String): String = filePath.substringAfterLast("/", "")

    //文件名字不带后缀
    fun filenameNoExtension(filePath: String): String = filename(filePath).substringBeforeLast(".", "")

    //文件后缀
    fun fileExtension(filePath: String): String = filePath.substringAfterLast(".", "")

    //file 转bitmap
    fun bitmap(file: File): Bitmap? {
        return if (!file.exists()) null
        else {
            val bytes = file.readBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    fun getVideoInfo(file: File, videoInfo: (duration: Int, width: Int, height: Int) -> Unit) {
        val mmr = MediaMetadataRetriever()
        var duration = "0"
        var width = "0"
        var height = "0"
        if (!file.path.isNullOrBlank()) {
            mmr.setDataSource(file.path)
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
}