package com.app.common.logger

class DiskLogAdapter(private val formatStrategy: FormatStrategy = CsvFormatStrategy.newBuilder().build()) : LogAdapter {

    override fun log(priority: Logger.Level, tag: String?, message: String) {
        formatStrategy.log(priority, tag, message)
    }

    override fun isLoggable(priority: Logger.Level, tag: String?) = true
}