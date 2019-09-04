package com.app.common.view.asyncLayout

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import androidx.core.view.LayoutInflaterCompat
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * https://www.jianshu.com/p/f0c0eda06ae4
 * 实现异步加载布局的功能，修改点：
 * 1. 单一线程；
 * 2. super.onCreate之前调用没有了默认的Factory；
 * 3. 排队过多的优化；
 */
class AsyncLayoutInflaterPlus(context: Context) {
    private val mHandler: Handler
    private val mInflater: LayoutInflater
    private var mInflateRunnable: InflateRunnable? = null
    private var future: Future<*>? = null

    init {
        mInflater = BasicInflater(context)
        mHandler = Handler(Handler.Callback { msg ->
            val request = msg.obj as InflateRequest
            if (request.view == null) {
                request.view = mInflater.inflate(request.resid, request.parent, false)
            }
            request.callback?.onInflateFinished(
                    request.view, request.resid, request.parent)
            request.countDownLatch?.countDown()
            releaseRequest(request)
            true
        })
    }

    /**
     * 判断这个任务是否已经开始执行
     *
     * @return
     */
    val isRunning: Boolean
        get() = mInflateRunnable?.isRunning ?: false


    @UiThread
    fun inflate(@LayoutRes resid: Int, parent: ViewGroup?, countDownLatch: CountDownLatch,
                callback: OnInflateFinishedListener) {
        val request = obtainRequest()
        request.inflater = this
        request.resid = resid
        request.parent = parent
        request.callback = callback
        request.countDownLatch = countDownLatch
        mInflateRunnable = InflateRunnable(request)
        future = sExecutor.submit(mInflateRunnable)
    }

    fun cancel() {
        future?.cancel(true)
    }

    interface OnInflateFinishedListener {
        fun onInflateFinished(view: View?, resid: Int, parent: ViewGroup?)
    }

    private inner class InflateRunnable(private val request: InflateRequest) : Runnable {
        var isRunning: Boolean = false
            private set

        override fun run() {
            isRunning = true
            try {
                request.view = request.inflater?.mInflater?.inflate(
                        request.resid, request.parent, false)
            } catch (ex: RuntimeException) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI" + " thread", ex)
            }

            Message.obtain(request.inflater?.mHandler, 0, request)
                    .sendToTarget()
        }
    }

    class InflateRequest internal constructor() {
        internal var inflater: AsyncLayoutInflaterPlus? = null
        internal var parent: ViewGroup? = null
        internal var resid: Int = 0
        internal var view: View? = null
        internal var callback: OnInflateFinishedListener? = null
        internal var countDownLatch: CountDownLatch? = null
    }

    private class BasicInflater internal constructor(context: Context) : LayoutInflater(context) {

        init {
            if (context is AppCompatActivity) {
                // 加上这些可以保证AppCompatActivity的情况下，super.onCreate之前
                // 使用AsyncLayoutInflater加载的布局也拥有默认的效果
                val appCompatDelegate = context.delegate
                if (appCompatDelegate is LayoutInflater.Factory2) {
                    LayoutInflaterCompat.setFactory2(this, appCompatDelegate as Factory2)
                }
            }
        }

        override fun cloneInContext(newContext: Context): LayoutInflater {
            return BasicInflater(newContext)
        }

        @Throws(ClassNotFoundException::class)
        override fun onCreateView(name: String, attrs: AttributeSet): View {
            for (prefix in sClassPrefixList) {
                try {
                    val view = createView(name, prefix, attrs)
                    if (view != null) {
                        return view
                    }
                } catch (e: ClassNotFoundException) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }

            }

            return super.onCreateView(name, attrs)
        }

        companion object {
            private val sClassPrefixList = arrayOf("android.widget.", "android.webkit.", "android.app.")
        }
    }

    private fun obtainRequest(): InflateRequest {
        var obj: InflateRequest? = sRequestPool.acquire()
        if (obj == null) {
            obj = InflateRequest()
        }
        return obj
    }

    private fun releaseRequest(obj: InflateRequest) {
        obj.callback = null
        obj.inflater = null
        obj.parent = null
        obj.resid = 0
        obj.view = null
        sRequestPool.release(obj)
    }

    companion object {
        private val TAG = "AsyncLayoutInflaterPlus"
        // 真正执行加载任务的线程池
        private val sExecutor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() - 2))
        // InflateRequest pool
        private val sRequestPool = Pools.SynchronizedPool<InflateRequest>(10)
    }

}