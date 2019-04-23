package com.app.common.logger

import androidx.annotation.NonNull
import androidx.annotation.Nullable

interface FormatStrategy {

    fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String)
}