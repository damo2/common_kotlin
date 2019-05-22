package com.app.common.logger

import LoggerPrinter
import androidx.annotation.NonNull
import androidx.annotation.Nullable

fun logw(message: Any) {
    Logger.w(message.toString())
}

fun logd(message: Any) {
    Logger.d(message.toString())
}

fun logi(message: Any) {
    Logger.i(message.toString())
}

fun loge(message: Any) {
    Logger.e(message.toString())
}

object Logger {

    enum class Level(val priority: Int) {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        ASSERT(7)
    }

    private var printer: Printer = LoggerPrinter().apply {
        addAdapter(checkNotNull(AndroidLogAdapter()))
    }

    fun printer(@NonNull printer: Printer) {
        this.printer = checkNotNull(printer)
    }

    fun addLogAdapter(@NonNull adapter: LogAdapter) = apply {
        printer.addAdapter(checkNotNull(adapter))
    }

    fun clearLogAdapters() {
        printer.clearLogAdapters()
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    fun t(@Nullable tag: String): Printer {
        return printer.t(tag)
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    fun log(priority: Level, @Nullable tag: String, @Nullable message: String, @Nullable throwable: Throwable) {
        printer.log(priority, tag, message, throwable)
    }

    fun d(@NonNull message: String, @Nullable vararg args: Any) {
        printer.d(message, args)
    }

    fun d(@Nullable any: Any) {
        printer.d(any)
    }

    fun e(@NonNull message: String, @Nullable vararg args: Any) {
        printer.e(null, message, args)
    }

    fun e(@Nullable throwable: Throwable, @NonNull message: String, @Nullable vararg args: Any) {
        printer.e(throwable, message, args)
    }

    fun i(@NonNull message: String, @Nullable vararg args: Any) {
        printer.i(message, args)
    }

    fun v(@NonNull message: String, @Nullable vararg args: Any) {
        printer.v(message, args)
    }

    fun w(@NonNull message: String, @Nullable vararg args: Any) {
        printer.w(message, args)
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    fun wtf(@NonNull message: String, @Nullable vararg args: Any) {
        printer.wtf(message, args)
    }
}