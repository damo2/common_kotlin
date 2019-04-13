package com.app.common.utils

import android.content.Context
import android.os.Environment
import android.os.StatFs
import java.io.File

object StorageUtils {

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取SD卡的可用空间大小（单位 byte）
     */
    fun getSDCardSpace(): Long {
        var sdCardSize: Long = 0
        if (isExternalStorageWritable()) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            sdCardSize = sf.availableBytes
        }
        return sdCardSize
    }
    /**
     * 获取SD卡剩余空间大小 （单位 byte）
     */
    private fun getDataDirSpace(): Long {
        val root = Environment.getDataDirectory()
        val sf = StatFs(root.path)
        return sf.availableBytes
    }

    // 获取SD卡的根目录 /storage/sdcard0
    fun getSDCardBaseDir(type: String? = null): String? =
            if (isExternalStorageWritable()) {
                if (type != null) Environment.getExternalStoragePublicDirectory(type).toString() else Environment.getExternalStorageDirectory().absolutePath
            } else null

    // 获取内部存储根目录 /data
    fun getDataBaseDir(): String = Environment.getDataDirectory().absolutePath

    /**
     * 获取SD卡公有目录的文件,SD卡卸载就取系统目录
     */
    fun getPublicStorageFile(filename: String, type: String? = null, create: Boolean = true): File? =
            createFile(getPublicStoragePathSub(filename, type), create)

    /**
     * 获取SD卡公有目录的目录文件,SD卡卸载就取系统目录
     */
    fun getPublicStorageDir(child: String, type: String? = null): String {
        val filePath = getPublicStoragePathSub(child, type)
        val file = File(filePath)
        file.mkdirs()
        return filePath
    }
    /**
     * 获取SD卡公有目录的文件路径,SD卡卸载就取系统目录
     */
    fun getPublicStoragePath(filename: String, type: String? = null): String {
        val filePath = getPublicStoragePathSub(filename, type)
        File(filePath).parentFile.mkdirs()
        return filePath
    }

    /**
     * 获取SD卡公有目录的文件,SD卡卸载就取系统目录
     */
    fun getSDCardPrivateCacheFile(context: Context, child: String? = null, create: Boolean = true): File? =
            createFile(getPrivateCachePathSub(context, child), create)


    /**
     * 获取缓存目录的文件目录，卸载APP 文件会删除
     */
    fun getPrivateCacheDir(context: Context, child: String? = null): String {
        val filePath: String = getPrivateCachePathSub(context, child)
        val file = File(filePath)
        file.mkdirs()
        return filePath
    }

    /**
     * 获取SD卡公有目录的路径,SD卡卸载就取系统目录
     */
    private fun getPublicStoragePathSub(filename: String, type: String? = null): String {
        val baseDir = getSDCardBaseDir(type) ?: getDataBaseDir()
        val filePath = """$baseDir${File.separator}$filename"""
        return filePath
    }

    /**
     * 获取私有缓存目录,SD卡卸载就取系统目录
     */
    private fun getPrivateCachePathSub(context: Context, childPath: String? = null): String {
        val baseDir: String
        if (isExternalStorageWritable()) {
            if (context.externalCacheDir != null) {
                baseDir = context.externalCacheDir.absolutePath//  /mnt/sdcard/Android/data/com.my.app/cache
            } else {
                baseDir = Environment.getExternalStorageDirectory().path//  /mnt/sdcard
            }
        } else {
            if (context.cacheDir != null) {
                baseDir = context.cacheDir.absolutePath//  /data/data/com.my.app/cache
            } else {
                baseDir = Environment.getDataDirectory().absolutePath//  /data
            }
        }
        val filePath = """$baseDir${File.separator}$childPath"""
        return filePath
    }

    /**
     * 获取文件
     * @param filePath 文件路径
     * @param create 不存在是否创建目录和文件，默认创建
     */
    private fun createFile(filePath: String, create: Boolean): File? {
        val file = File(filePath)
        return if (file.exists()) {
            file
        } else {
            if (create) {
                file.parentFile.mkdirs()
                file.createNewFile()
                file
            } else
                null
        }
    }

}