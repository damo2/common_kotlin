package com.app.common.encrypt

import com.app.common.encrypt.type.AESEncrypt

/**
 * Created by wangru
 * Date: 2018/7/3  15:53
 * mail: 1902065822@qq.com
 * describe:
 */

class EncryptKt private constructor() {
    var mEncrypt: IEncrypt = AESEncrypt()

    companion object {
        val instance: EncryptKt by lazy {
            EncryptKt()
        }
    }

    /**
     * 加密
     */
    fun encrypt(data: String, key: String): String {
        return mEncrypt.encrypt(data, key)
    }

    /**
     * 解密
     */
    fun decrypt(data: String, key: String): String {
        return mEncrypt.decrypt(data, key)
    }

}
