import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.app.common.logger.LogAdapter
import com.app.common.logger.Logger
import com.app.common.logger.Logger.Level.*
import com.app.common.logger.Printer
import java.io.PrintWriter
import java.io.StringWriter

class LoggerPrinter : Printer {

    /**
     * Provides one-time used tag for the log message
     */
    private val localTag = ThreadLocal<String>()

    private val logAdapters = mutableListOf<LogAdapter>()

    /**
     * @return the appropriate tag based on local or global
     */
    private val tag: String?
        @Nullable get() {
            val tag = localTag.get()
            if (tag != null) {
                localTag.remove()
                return tag
            }
            return null
        }

    override fun t(@Nullable tag: String?): Printer {
        if (tag != null) {
            localTag.set(tag)
        }
        return this
    }

    override fun d(@NonNull message: String, @Nullable vararg args: Any) {
        log(DEBUG, null, message, args)
    }

    override fun d(@Nullable any: Any) {
        log(DEBUG, null, any.toString())
    }

    override fun e(@NonNull message: String, @Nullable vararg args: Any) {
        e(null, message, *args)
    }

    override fun e(throwable: Throwable?, message: String?, @Nullable vararg args: Any) {
        log(ERROR, throwable, message, args)
    }

    override fun w(@NonNull message: String, @Nullable vararg args: Any) {
        log(WARN, null, message, args)
    }

    override fun i(@NonNull message: String, @Nullable vararg args: Any) {
        log(INFO, null, message, args)
    }

    override fun v(@NonNull message: String, @Nullable vararg args: Any) {
        log(VERBOSE, null, message, args)
    }

    override fun wtf(@NonNull message: String, @Nullable vararg args: Any) {
        log(ASSERT, null, message, *args)
    }


    @Synchronized
    override fun log(priority: Logger.Level,
                     @Nullable tag: String?,
                     @Nullable message: String?,
                     @Nullable throwable: Throwable?) {
        var localMessage = message
        throwable?.let {
            localMessage += if (message != null) " : " + getThrowableString(throwable)
            else getThrowableString(throwable)
        }
        if (message.isNullOrEmpty()) {
            localMessage = "Empty/NULL log message"
        }
        logAdapters
                .filter { it.isLoggable(priority, tag) }
                .forEach { it.log(priority, tag, localMessage!!) }
    }

    override fun clearLogAdapters() {
        logAdapters.clear()
    }

    override fun addAdapter(@NonNull adapter: LogAdapter) {
        logAdapters.add(checkNotNull(adapter))
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    @Synchronized private fun log(priority: Logger.Level,
                                  @Nullable throwable: Throwable?,
                                  @NonNull msg: String?,
                                  @Nullable vararg args: Any) {
        checkNotNull(msg)

        val tag = tag
        val message = createMessage(msg, *args)
        log(priority, tag, message, throwable)
    }

    @NonNull private fun createMessage(message: String?, @Nullable vararg args: Any): String {
        return message?.let {
            return if (args.isEmpty()) message else {
                try {
                    String.format(message, *args)
                } catch (e: Exception) {
                    try {
                        args.toString()
                    } catch (e: Exception) {
                        ""
                    }
                }
            }
        } ?: ""

    }

    private fun getThrowableString(throwable: Throwable?): String {
        return throwable?.let {
            val writer = StringWriter()
            PrintWriter(writer).use {
                throwable.printStackTrace(it)
                it.flush()
            }
            writer.toString()
        } ?: ""
    }
} 