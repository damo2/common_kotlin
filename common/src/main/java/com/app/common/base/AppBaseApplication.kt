package com.app.common.base

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.app.common.BuildConfig
import com.app.common.imageloader.ImageLoader
import com.app.common.utils.ActivityStack
import com.app.common.view.ToastX
import kotlin.properties.Delegates


/**
 * AppBaseApplication 管理Activity，低内存处理
 * Created by wangru
 * Date: 2018/7/24  13:25
 * mail: 1902065822@qq.com
 * describe:
 */

open class AppBaseApplication : Application() {
    private val TAG = "AppBaseApplication"

    companion object {
        var instanceBase: AppBaseApplication by Delegates.notNull()
        val toast: ToastX by lazy { ToastX(instanceBase) }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
        addActivityListener()
    }

    override fun onCreate() {
        instanceBase = this
        super.onCreate()
    }

    override fun onTerminate() {
        Log.d(TAG, "程序终止")
        super.onTerminate()
    }

    override fun onLowMemory() {
        Log.d(TAG, "低内存")
        super.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d(TAG, "配置改变")
        super.onConfigurationChanged(newConfig)
    }

    override fun onTrimMemory(level: Int) {
        Log.d(TAG, "内存清理$level")
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            ImageLoader.loader().clearMemory(this)
        }
    }

    private fun addActivityListener() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
                Log.d(TAG, "onActivityCreated: " + activity.localClassName)
                ActivityStack.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                Log.d(TAG, "onActivityDestroyed: " + activity.localClassName)
                ActivityStack.finish(activity)
            }
        })
    }

    fun exitApp() {
        ActivityStack.showAll()
        ActivityStack.finishAll()
        System.exit(0)
    }
}
