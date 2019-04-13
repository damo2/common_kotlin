package com.app.common.utils

import android.Manifest
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * Created by wangru
 * Date: 2018/6/29  16:18
 * mail: 1902065822@qq.com
 * describe:
 */

object PermissionData {
    /**
     * 读写文件 所需权限
     */
    val READWRITE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    const val READWRITE_CODE = 101
    /**
     * 相机 所需权限
     */
    val CAMERA = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    const val CAMERA_CODE = 102

    /**
     * 相机 所需权限
     */
    val VIDEO = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    const val VIDEO_CODE = 103
    /**
     * 相册 所需权限
     */
    val ALBUM = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    const val ALBUM_CODE = 104
    /**
     * 定位 所需权限
     */
    val LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    const val LOCATION_CODE = 105
    /**
     * 语音 所需权限
     */
    val VOICE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    const val VOICE_CODE = 106
}
