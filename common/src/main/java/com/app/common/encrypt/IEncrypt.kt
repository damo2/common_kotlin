package com.app.common.encrypt

/**
 * Created by wangru
 * Date: 2018/7/3  15:48
 * mail: 1902065822@qq.com
 * describe:
 */

interface IEncrypt {

    fun encrypt(data: String, key: String): String

    fun decrypt(data: String, key: String): String

    val key: String

}
