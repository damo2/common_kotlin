package com.app.common.logger

import android.os.HandlerThread
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.app.common.utils.StorageUtils
import java.text.SimpleDateFormat
import java.util.*


class CsvFormatStrategy(builder: Builder) : FormatStrategy {

    companion object {
        val NEW_LINE = System.getProperty("line.separator")
        val NEW_LINE_REPLACEMENT = " <br> "
        val SEPARATOR = ","

        @NonNull
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    private var date = builder.date
    private var dateFormat = builder.dateFormat
    private var logStrategy = builder.logStrategy!!
    private var tag: String = builder.tag

    override fun log(priority: Logger.Level, @Nullable onceOnlyTag: String?, @NonNull message: String) {
        var localMessage = message

        val tag = formatTag(onceOnlyTag)

        val builder = StringBuilder()

        // machine-readable date/time
        builder.append(date.time.toString())

        // human-readable date/time
        builder.append(SEPARATOR)
        builder.append(dateFormat!!.format(date))

        // level
        builder.append(SEPARATOR)
        builder.append(priority.name)

        // tag
        builder.append(SEPARATOR)
        builder.append(tag)

        // localMessage
//        if (localMessage.contains(NEW_LINE)) {
//            // a new line would break the CSV format, so we replace it here
//            localMessage = localMessage.replace(NEW_LINE.toRegex(), NEW_LINE_REPLACEMENT)
//        }
        builder.append(SEPARATOR)
        builder.append(localMessage)

        // new line
        builder.append(NEW_LINE)

        logStrategy.log(priority, tag, builder.toString())
    }

    @Nullable private fun formatTag(@Nullable tag: String?): String {
        return if (this.tag != tag) {
            this.tag + "-" + tag
        } else this.tag
    }

    class Builder internal constructor() {

        internal var date: Date = Date()
        internal var dateFormat: SimpleDateFormat? = null
        internal var logStrategy: LogStrategy? = null
        internal var tag = "PRETTY_LOGGER"

        @NonNull
        fun dateFormat(@Nullable format: SimpleDateFormat) = apply { this.dateFormat = format }

        @NonNull
        fun logStrategy(@Nullable strategy: LogStrategy) = apply { this.logStrategy = strategy }

        @NonNull
        fun tag(@Nullable tag: String): Builder {
            this.tag = tag
            return this
        }

        @NonNull
        fun build(): CsvFormatStrategy {
            if (dateFormat == null) {
                dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK)
            }
            if (logStrategy == null) {
                val folder = StorageUtils.getPublicStorageDir("logger")
                val ht = HandlerThread("AndroidFileLogger." + folder)
                ht.start()
                val handler = DiskLogStrategy.WriteHandler(ht.looper, folder, MAX_BYTES)
                logStrategy = DiskLogStrategy(handler)
            }
            return CsvFormatStrategy(this)
        }

        companion object {
            private val MAX_BYTES = 100 * 1024 // 100K averages to a 4000 lines per file
        }
    }

}