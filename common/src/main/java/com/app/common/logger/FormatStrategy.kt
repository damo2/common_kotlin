package com.app.common.logger

import android.support.annotation.NonNull
import android.support.annotation.Nullable

interface FormatStrategy {

    fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String)
}