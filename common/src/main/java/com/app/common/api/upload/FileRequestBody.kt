package com.app.common.api.upload

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

import java.io.IOException

/**
 * Created by wangru
 * Date: 2018/7/25  20:09
 * mail: 1902065822@qq.com
 * describe:
 */

class FileRequestBody<T>(
        //实际请求体
        private val requestBody: RequestBody,
        // 上传回调接口
        private val callback: FileUpLoadObserver<T>) : RequestBody() {

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        Okio.buffer(sink(sink))?.let {
            //写入
            requestBody.writeTo(it)
            //必须调用flush，否则最后一部分数据可能不会被写入
            it.flush()
        }
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink = object : ForwardingSink(sink) {
        //当前写入字节数
        internal var bytesWritten = 0L
        //总字节长度，避免多次调用contentLength()方法
        internal var contentLength = 0L

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (contentLength == 0L) {
                //获得contentLength的值，后续不再调用
                contentLength = contentLength()
            }
            //增加当前写入的字节数
            bytesWritten += byteCount
            //回调
            callback.upProgressCallback?.invoke(bytesWritten, contentLength)
        }
    }
}


