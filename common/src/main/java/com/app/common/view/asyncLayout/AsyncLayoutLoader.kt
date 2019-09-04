package com.app.common.view.asyncLayout

import android.content.Context
import android.content.res.XmlResourceParser
import android.util.AttributeSet
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.concurrent.CountDownLatch

import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.collection.SparseArrayCompat

/**
 * https://www.jianshu.com/p/f0c0eda06ae4
 * 调用入口类；同时解决加载和获取View在不同类的场景
 */
class AsyncLayoutLoader private constructor(private val mContext: Context) {

    private var mLayoutId: Int = 0
    private var mRealView: View? = null
    private var mRootView: ViewGroup? = null
    private val mCountDownLatch: CountDownLatch = CountDownLatch(1)
    private var mInflater: AsyncLayoutInflaterPlus = AsyncLayoutInflaterPlus(mContext)

    /**
     * getLayoutLoader 和 getRealView 方法配对出现
     * 用于加载和获取View在不同类的场景
     *
     * @return
     */
    val realView: View?
        get() {
            if (mRealView == null && !mInflater.isRunning) {
                mInflater.cancel()
                inflateSync()
            } else if (mRealView == null) {
                try {
                    mCountDownLatch.await()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                setLayoutParamByParent(mContext, mRootView, mLayoutId, mRealView)
            } else {
                setLayoutParamByParent(mContext, mRootView, mLayoutId, mRealView)
            }
            return mRealView
        }

    @UiThread
    @JvmOverloads
    fun inflate(@LayoutRes resid: Int, parent: ViewGroup?,
                listener: AsyncLayoutInflaterPlus.OnInflateFinishedListener? = null) {
        var listenerNew = listener
        mRootView = parent
        mLayoutId = resid
        sArrayCompat.append(mLayoutId, this)
        if (listenerNew == null) {
            listenerNew = object : AsyncLayoutInflaterPlus.OnInflateFinishedListener {
                override fun onInflateFinished(view: View?, resid: Int, parent: ViewGroup?) {
                    mRealView = view
                }
            }
        }
        mInflater.inflate(resid, parent, mCountDownLatch, listenerNew)
    }

    private fun inflateSync() {
        mRealView = LayoutInflater.from(mContext).inflate(mLayoutId, mRootView, false)
    }

    companion object {
        private val sArrayCompat = SparseArrayCompat<AsyncLayoutLoader>()

        fun getInstance(context: Context): AsyncLayoutLoader {
            return AsyncLayoutLoader(context)
        }

        /**
         * getLayoutLoader 和 getRealView 方法配对出现
         * 用于加载和获取View在不同类的场景
         *
         * @param resid
         * @return
         */
        fun getLayoutLoader(resid: Int): AsyncLayoutLoader? {
            return sArrayCompat.get(resid)
        }


        /**
         * 根据Parent设置异步加载View的LayoutParamsView
         *
         * @param context
         * @param parent
         * @param layoutResId
         * @param view
         */
        private fun setLayoutParamByParent(context: Context, parent: ViewGroup?, layoutResId: Int, view: View?) {
            if (parent == null) {
                return
            }
            val parser = context.resources.getLayout(layoutResId)
            try {
                val attrs = Xml.asAttributeSet(parser)
                val params = parent.generateLayoutParams(attrs)
                view?.layoutParams = params
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                parser.close()
            }
        }
    }

}