package com.app.common.file

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.app.common.extensions.getApplicationIdExt
import java.io.File
import java.io.FileNotFoundException


/**
 * 下载文件后通知系统更新
 * Created by wangru
 * Date: 2018/6/1  14:37
 * mail: 1902065822@qq.com
 * describe:
 */

object UpdateFile {

    private val TAG = "UpdateFile"

    /**
    App 模块AndroidManifest 添加
    <meta-data
    android:name="APPLICATION_ID"
    android:value="${applicationId}"/>
     */
    fun getUriFromFile(context: Context?, file: File): Uri {
        val imageUri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "${context.getApplicationIdExt()}.fileprovider", file)
        } else {
            imageUri = Uri.fromFile(file)
        }
        return imageUri
    }

    /**
     * 保存文件通知系统更新，在图库显示图片
     *
     * @param applicationId applicationId eg: com.app.test
     */
    fun updateImageSysStatu(context: Context?, path: String, applicationId: String) {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (context != null && file.exists()) {
                // 把文件插入到系统图库
                try {
                    //insertImage 可能会有2个，系统也生成一个
//                    MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, "image", "图片")
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                updateFileStatu(context, file)
            }

        } else {
            Log.d(TAG, "updateFileStatu: path is null")
        }
    }

    /**
     * 保存文件通知系统更新，在图库显示图片
     * @param
     */
    fun updateFileStatu(context: Context?, file: File?) {
        if (context != null && file != null && file.exists()) {
            //通知图库更新
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri: Uri = getUriFromFile(context, file)
                intent.data = uri
                context.sendBroadcast(intent)
            } else {
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())))
            }
        } else {
            Log.d(TAG, "updateFileStatu: file is not exist")
        }
    }
}
