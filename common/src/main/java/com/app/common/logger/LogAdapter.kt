package com.app.common.logger

import android.support.annotation.NonNull
import android.support.annotation.Nullable

/**
 * Created by lemo-wu on 2018/7/12.
 */
interface LogAdapter {

    /**
     * Used to determine whether log should be printed out or not.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message
     *
     * @return is used to determine if log should printed.
     * If it is true, it will be printed, otherwise it'll be ignored.
     */
    fun isLoggable(priority: Logger.Level, @Nullable tag: String?): Boolean

    /**
     * Each log will use this pipeline
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     */
    fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String)
}