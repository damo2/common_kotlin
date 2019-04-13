package com.app.common.logger

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import java.io.File
import java.io.FileWriter

class DiskLogStrategy(private val handler: Handler) : LogStrategy {

    override fun log(priority: Logger.Level, @Nullable tag: String?, @NonNull message: String) {
        checkNotNull(message)

        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(priority.priority, message))
    }

    internal class WriteHandler(@NonNull looper: Looper, folder: String, private val maxFileSize: Int) : Handler(checkNotNull(looper)) {

        private var folder: String = checkNotNull(folder)

        override fun handleMessage(@NonNull msg: Message) {
            val content = msg.obj as String
            val logFile = getLogFile(folder, "logs")
            if (!logFile.exists()) logFile.createNewFile()
            logFile.appendText(content)
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private fun writeLog(@NonNull fileWriter: FileWriter, @NonNull content: String) {
            checkNotNull(fileWriter)
            checkNotNull(content)

            fileWriter.append(content)
        }

        private fun getLogFile(@NonNull folderName: String, @NonNull fileName: String): File {
            checkNotNull(folderName)
            checkNotNull(fileName)
            val folder = File(folderName)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            var newFileCount = 0
            var newFile: File
            var existingFile: File? = null

            newFile = File(folder, String.format("%s_%s.csv", fileName, newFileCount))
            while (newFile.exists()) {
                existingFile = newFile
                newFileCount++
                newFile = File(folder, String.format("%s_%s.csv", fileName, newFileCount))
            }

            return if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    newFile
                } else existingFile
            } else newFile

        }
    }
}