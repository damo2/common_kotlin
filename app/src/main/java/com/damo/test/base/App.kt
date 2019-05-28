package com.damo.test.base

import cn.jpush.android.api.JPushInterface
import com.alibaba.android.alpha.AlphaManager
import com.alibaba.android.alpha.Project
import com.alibaba.android.alpha.Task
import com.app.common.base.AppBaseApplication
import com.app.common.logger.logd
import com.damo.test.BuildConfig
import com.damo.test.api.ApiManager
import com.didichuxing.doraemonkit.DoraemonKit
import kotlin.properties.Delegates
import com.app.common.logger.AndroidLogAdapter
import com.app.common.logger.DiskLogAdapter
import com.app.common.logger.Logger


class App : AppBaseApplication() {
    companion object {
        var instance: App by Delegates.notNull()
        val isDebug: Boolean = BuildConfig.DEBUG
    }

    override fun onCreate() {
        instance = this
        createProject()
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
    inner class LoggerTask : Task("LoggerTask") {
        override fun run() {
            logd("ApiServiceTask")
            //初始化
            Logger.addLogAdapter(DiskLogAdapter())
        }
    }
    inner class ApiServiceTask : Task("SampleTask") {
        override fun run() {
            logd("ApiServiceTask")
            ApiManager.initApiService()
        }
    }

    inner class JPushTask : Task("SampleTask") {
        override fun run() {
            logd("JPushTask")
            initJPush()
        }
    }

    inner class DoraemonKitTask : Task("SampleTask") {
        override fun run() {
            logd("DoraemonKitTask")
            initDoraemonKit()
        }
    }

    private fun createCommonTaskGroup(): Task {
        val builder = Project.Builder()
        builder.add(ApiServiceTask())
        builder.add(DoraemonKitTask())
        builder.add(LoggerTask())
        return builder.create()
    }

    private fun createProject() {
        val group = createCommonTaskGroup()
        val jPushTask = JPushTask()
        val builder = Project.Builder()
        builder.add(group)
        builder.add(jPushTask)
        val project = builder.create()

        AlphaManager.getInstance(applicationContext).addProject(project);
        AlphaManager.getInstance(applicationContext).start();
    }
}
