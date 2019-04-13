package com.app.common.utils

import android.os.Looper
import com.app.common.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * 线程工具
 * Created by wangru
 * Date: 2018/7/20  14:59
 * mail: 1902065822@qq.com
 * describe:
 */
object ThreadUtil {
    fun lopper(block: () -> Unit) {
        Logger.d("线程：${Thread.currentThread().name}")
        val isNullLooper = Looper.myLooper() == null
        if (isNullLooper) {
            Looper.prepare()
        }
        block()
        if (isNullLooper) {
            Looper.loop()
        }
    }

    //下载线程池
    val threadPoolSaveFile by lazy { ThreadPoolExecutor(6, 200, 1, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>()) }
}