package com.damo.test.base

import cn.jpush.android.api.JPushInterface
import com.app.common.base.AppBaseApplication
import com.damo.test.BuildConfig
import com.damo.test.api.ApiManager
import com.didichuxing.doraemonkit.DoraemonKit
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
        initDoraemonKit()
        super.onCreate()
    }

    private fun initDoraemonKit() {
        DoraemonKit.install(instance)
        // H5任意门功能需要，非必须
        DoraemonKit.setWebDoorCallback { context, url ->
            // 使用自己的H5容器打开这个链接
        }
    }

    private fun initJPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        JPushInterface.setAlias(applicationContext, 1, "s")
    }
}
