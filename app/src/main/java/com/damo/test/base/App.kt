package com.damo.test.base

import com.app.common.base.AppBaseApplication
import com.damo.test.BuildConfig
import com.damo.test.api.ApiManager
import kotlin.properties.Delegates


class App : AppBaseApplication() {
    companion object {
        var instance: App by Delegates.notNull()
        val isDebug: Boolean = BuildConfig.DEBUG
    }

    override fun onCreate() {
        instance = this
        ApiManager.initApiService();
        super.onCreate()
    }
}
