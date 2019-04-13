package com.app.common.logger

import android.support.annotation.NonNull
import android.support.annotation.Nullable

/**
 * Created by lemo-wu on 2018/7/12.
 */
interface LogStrategy {

    /**
     * This is invoked by Logger each time a log message is processed.
     * Interpret this method as last destination of the log in whole pipeline.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     */
    fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String)

}