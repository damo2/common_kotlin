package com.damo.test.base

import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.app.common.base.AppBaseApplication
import com.damo.test.BuildConfig
import com.damo.test.api.ApiManager
import kotlin.math.log
import kotlin.properties.Delegates


class App : AppBaseApplication() {
    companion object {
        var instance: App by Delegates.notNull()
        val isDebug: Boolean = BuildConfig.DEBUG
    }

    override fun onCreate() {
        instance = this
        ApiManager.initApiService()
        initJPush()
        super.onCreate()
    }

    private fun initJPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        JPushInterface.setAlias(applicationContext, "s", { code, _, _ -> { Log.d("jpush","jpush code=$code")} })
        JPushInterface.setAlias(applicationContext,1,"s")
    }
}
