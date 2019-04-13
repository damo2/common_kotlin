package com.app.common.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.app.common.extensions.copyTo
import com.app.common.file.UpdateFile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by wangru
 * Date: 2018/5/22  10:08
 * mail: 1902065822@qq.com
 * describe:
 */

object CacheImgUtil {
    private val TAG = "CacheImgUtil"
    val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
    internal var mFixedThreadPool: ExecutorService = ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue())
    val hanler= Handler(Looper.getMainLooper())
    fun downImage(context: Context, url: String, pathSave: String, packageName: String, downCallback: (isSuc: Boolean) -> Unit) {
        mFixedThreadPool.execute {
            try {
                Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get().copyTo(pathSave)
                UpdateFile.updateImageSysStatu(context, pathSave, packageName)
                hanler.post {
                    downCallback(true)
                }
            } catch (e: Exception) {
                hanler.post {
                    downCallback(false)
                }
            }
        }
    }


}
