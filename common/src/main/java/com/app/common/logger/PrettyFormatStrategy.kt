package com.app.common.logger

import LoggerPrinter
import android.support.annotation.NonNull
import android.support.annotation.Nullable

class PrettyFormatStrategy(builder: Builder) : FormatStrategy {

    companion object {
        private val CHUNK_SIZE = 4000
        private val MIN_STACK_OFFSET = 5
        private val TOP_LEFT_CORNER = '┌'
        private val BOTTOM_LEFT_CORNER = '└'
        private val MIDDLE_CORNER = '├'
        private val HORIZONTAL_LINE = '│'
        private val DOUBLE_DIVIDER = "────────────────────────────────────────────────────────"
        private val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
        private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER

        fun newBuilder(): Builder {
            return Builder()
        }
    }

    /**
     * Drawing toolbox
     */


    private var methodCount: Int = builder.methodCount
    private var methodOffset: Int = builder.methodOffset
    private var showThreadInfo: Boolean = builder.showThreadInfo
    private var logStrategy: LogStrategy = builder.logStrategy!!
    private var tag: String = builder.tag

    override fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String) {
        checkNotNull(message)

        val localTag = formatTag(tag)

        logTopBorder(priority, localTag)
        logHeaderContent(priority, localTag, methodCount)

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        val bytes = message.toByteArray()
        val length = bytes.size
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(priority, localTag)
            }
            logContent(priority, localTag, message)
            logBottomBorder(priority, localTag)
            return
        }
        if (methodCount > 0) {
            logDivider(priority, localTag)
        }
        var i = 0
        while (i < length) {
            val count = Math.min(length - i, CHUNK_SIZE)
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, localTag, String(bytes, i, count))
            i += CHUNK_SIZE
        }
        logBottomBorder(priority, localTag)
    }

    private fun logTopBorder(logType: Logger.Level, @Nullable tag: String) {
        logChunk(logType, tag, TOP_BORDER)
    }

    private fun logHeaderContent(logType: Logger.Level, @Nullable tag: String, methodCount: Int) {
        var methodCount = methodCount
        val trace = Thread.currentThread().stackTrace
        if (showThreadInfo) {
            logChunk(logType, tag, HORIZONTAL_LINE + " Thread: " + Thread.currentThread().name)
            logDivider(logType, tag)
        }
        var level = ""

        val stackOffset = getStackOffset(trace) + methodOffset

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.size) {
            methodCount = trace.size - stackOffset - 1
        }

        for (i in methodCount downTo 1) {
            val stackIndex = i + stackOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append(HORIZONTAL_LINE)
                    .append(' ')
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].className))
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            logChunk(logType, tag, builder.toString())
        }
    }

    private fun logBottomBorder(logType: Logger.Level, @Nullable tag: String) {
        logChunk(logType, tag, BOTTOM_BORDER)
    }

    private fun logDivider(logType: Logger.Level, @Nullable tag: String) {
//        logChunk(logType, tag, MIDDLE_BORDER)
    }

    private fun logContent(logType: Logger.Level, @Nullable tag: String, @NonNull chunk: String) {
        checkNotNull(chunk)

        val lines = chunk.split(System.getProperty("line.separator").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            logChunk(logType, tag, HORIZONTAL_LINE + " " + line)
        }
    }

    private fun logChunk(priority: Logger.Level, @Nullable tag: String, @NonNull chunk: String) {
        checkNotNull(chunk)

        logStrategy.log(priority, tag, chunk)
    }

    private fun getSimpleClassName(@NonNull name: String): String {
        checkNotNull(name)

        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private fun getStackOffset(@NonNull trace: Array<StackTraceElement>): Int {
        checkNotNull(trace)

        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LoggerPrinter::class.java.name && name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    @Nullable private fun formatTag(@Nullable tag: String?): String {
        return if (this.tag != tag) {
            this.tag + "-" + tag
        } else this.tag
    }

    class Builder {
        internal var methodCount = 2
        internal var methodOffset = 0
        internal var showThreadInfo = true
        @Nullable internal var logStrategy: LogStrategy? = null
        @Nullable internal var tag = "PRETTY_LOGGER"

        @NonNull
        fun methodCount(methodCount: Int) = apply { this.methodCount = methodCount }

        @NonNull
        fun methodOffset(methodOffset: Int) = apply { this.methodOffset = methodOffset }

        @NonNull
        fun showThreadInfo(showThreadInfo: Boolean) = apply { this.showThreadInfo = showThreadInfo }

        @NonNull
        fun logStrategy(logStrategy: LogStrategy) = apply { this.logStrategy = logStrategy }

        @NonNull
        fun tag(@Nullable tag: String) = apply { this.tag = tag }

        @NonNull
        fun build(): PrettyFormatStrategy {
            if (logStrategy == null) {
                logStrategy = LogcatLogStrategy()
            }
            return PrettyFormatStrategy(this)
        }
    }
}