package com.app.common.utils

import android.app.Activity
import android.support.annotation.NonNull
import com.app.common.logger.Logger
import java.util.*

/**
 * 管理Activity
 */
object ActivityStack {

    private val activitys: Stack<Activity> by lazy {
        Stack<Activity>()
    }

    val size = activitys.size

    fun add(@NonNull activity: Activity) {
        checkNotNull(activity)
        activitys.push(activity)
    }

    fun top(): Activity? {
        return activitys.lastOrNull()
    }

    fun find(clazz: Class<Activity>): Activity? {
        return if (activitys.isEmpty()) {
            null
        } else {
            activitys.firstOrNull {
                it.javaClass == clazz
            }
        }
    }

    fun finish(activity: Activity? = activitys.lastOrNull()) {
        activitys.removeAll {
            it == activity
        }
    }

    fun finish(@NonNull clazz: Class<Activity>) {
        activitys.removeAll {
            it.javaClass == clazz
        }
    }

    fun finishExclude(clazz: Class<Activity>) {
        activitys.removeAll {
            it.javaClass != clazz
        }
    }

    fun finishAll() {
        activitys.forEach {
            if (it.isFinishing.not()) {
                it.finish()
            }
        }
        activitys.clear()
    }

    fun showAll() {
        activitys.forEach {
            Logger.d(it.localClassName)
        }
    }
}