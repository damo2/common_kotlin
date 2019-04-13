package com.app.common.api.download

import com.google.gson.JsonSyntaxException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


/**
 * Created by wangru
 * Date: 2018/7/6  16:12
 * mail: 1902065822@qq.com
 * describe:
 */

abstract class FileDownLoadObserver<T> : Observer<T> {

    override fun onNext(t: T) {
        onDownLoadSuccess(t)
    }

    override fun onError(e: Throwable) {
        onDownLoadFail(e)
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    //下载成功的回调
    abstract fun onDownLoadSuccess(t: T)

    //下载失败回调
    abstract fun onDownLoadFail(throwable: Throwable)

    /**
     * 将文件写入本地
     * @param responseBody 请求结果全体
     * @return 写入完成的文件
     */
    fun saveFile(responseBody: ResponseBody, file: File): File {
        if (file.exists()) file.delete()
        val fileBuf = File("${file.path}_down")
        if (fileBuf.exists()) fileBuf.delete()
        val buf = ByteArray(2048)
        var sum: Long = 0
        var len = 0
        responseBody.byteStream().use { inputStream ->
            FileOutputStream(fileBuf).use { fos ->
                while ((inputStream.read(buf).also { len = it }) != -1) {
                    sum += len.toLong()
                    fos.write(buf, 0, len)
                }
                fileBuf.renameTo(file)
            }
            return file
        }
    }

    fun getErrorInfo(e: Throwable) = if (e is ConnectException || e is UnknownHostException) {
        "网络未连接"
    } else if (e is TimeoutException || e is SocketTimeoutException) {
        "网络超时"
    } else if (e is JsonSyntaxException) {
        "数据解析异常"
    } else {
        "请求异常"
    }
}