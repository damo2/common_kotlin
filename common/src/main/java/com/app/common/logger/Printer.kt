package com.app.common.logger

import android.support.annotation.NonNull

interface Printer {

    fun t(tag: String?): Printer

    fun d(message: String, vararg args: Any)

    fun d(any: Any)

    fun e(message: String, vararg args: Any)

    fun e(throwable: Throwable?, message: String? = null, vararg args: Any)

    fun w(message: String, vararg args: Any)

    fun i(message: String, vararg args: Any)

    fun v(message: String, vararg args: Any)

    fun wtf(message: String, vararg args: Any)

    fun log(priority: Logger.Level, tag: String?, message: String?, throwable: Throwable?)

    fun addAdapter(@NonNull adapter: LogAdapter)

    fun clearLogAdapters()
}