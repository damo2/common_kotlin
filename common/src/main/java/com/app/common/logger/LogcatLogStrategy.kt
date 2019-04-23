package com.app.common.logger

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class LogcatLogStrategy : LogStrategy {

    override fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String) {
        checkNotNull(message)
        Log.println(priority.priority, tag ?: DEFAULT_TAG, message)


    }

    companion object {
        internal val DEFAULT_TAG = "NO_TAG"
    }
}