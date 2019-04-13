package com.app.common.logger


class AndroidLogAdapter(private val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder().build()) : LogAdapter {

    override fun isLoggable(priority: Logger.Level, tag: String?) = true

    override fun log(priority: Logger.Level, tag: String?, message: String) {
        formatStrategy.log(priority, tag, message)
    }
}