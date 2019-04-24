package com.app.common

/**
 * Created by wr
 * Date: 2018/11/29  19:38
 * describe:
 */
object CommonConst {
    const val REQUEST_OUTTIME = 10_000L
    //超时时间 ms
    const val DOWNLOAD_OUTTIME = 16_000L
    const val UPLOAD_OUTTIME = 16_000L
    //retrofit baseurl 必须以“/”结尾
    var BASE_URL = "http://www.baidu.com/"

    fun setBaseUrl(url: String) {
        BASE_URL = url;
    }
}