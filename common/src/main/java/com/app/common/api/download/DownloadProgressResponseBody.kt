package com.app.common.api.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

import java.io.IOException

/**
 * Created by wr
 * Date: 2018/11/1  10:52
 * describe:
 */

class DownloadProgressResponseBody(private val responseBody: ResponseBody,
                                   private val progressCallback: (totalLength: Long, contentLength: Long, done: Boolean) -> Unit) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressCallback(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }

    }
}

