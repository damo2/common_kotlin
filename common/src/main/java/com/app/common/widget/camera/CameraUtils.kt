package com.app.common.widget.camera

import android.hardware.Camera

/**
 * Created by wr
 * Date: 2019/10/11  11:22
 * mail: 1902065822@qq.com
 * describe:
 */
object CameraUtils {

    //获取最佳预览大小
    fun getBestPreviewSize(width: Int, height: Int, parameters: Camera.Parameters): Camera.Size? {
        var result: Camera.Size? = null
        for (size in parameters.supportedPreviewSizes) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size
                } else {
                    val resultArea = result.width * result.height
                    val newArea = size.width * size.height
                    if (newArea > resultArea) {
                        result = size
                    }
                }
            }
        }
        return result
    }

    //获取最佳fps大小
    fun getBestPreviewFrameRate(fps: Int, parameters: Camera.Parameters): Int {
        parameters.supportedPreviewFrameRates?.let {
            if (it.size > 0) {
                var hasSupportRate = false
                it.sort()
                for (i in it.indices) {
                    val supportRate = it[i]
                    if (supportRate == 15) {
                        hasSupportRate = true
                    }
                }
                return if (hasSupportRate) {
                    15
                } else {
                    it[0]
                }
            }
        }
        return fps
    }
}