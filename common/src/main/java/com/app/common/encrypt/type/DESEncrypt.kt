package com.app.common.encrypt.type

import com.app.common.encrypt.IEncrypt
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by wangru
 * Date: 2018/7/3  20:27
 * mail: 1902065822@qq.com
 * describe:
 */

class DESEncrypt : IEncrypt {
    override val key: String get() = "qwerasdf"

    private val iv = byteArrayOf(0x12, 0x34, 0x56, 0x78, 0x90.toByte(), 0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte())

    //-----------------加密-----------------
    override fun encrypt(data: String, key: String): String {
        try {
            check(data.isNotBlank())
            val zeroIv = IvParameterSpec(iv)
            val secretKey = SecretKeySpec(key.toByteArray(), "DES")
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, zeroIv)
            val encryptedData = cipher.doFinal(data.toByteArray())
            return Base64.encode(encryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    override fun decrypt(data: String, key: String): String {
        try {
            check(data.isNotBlank())
            val byteMi = Base64.decode(data)
            val zeroIv = IvParameterSpec(iv)
            val secretKey = SecretKeySpec(key.toByteArray(), "DES")
            val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, zeroIv)
            return String(cipher.doFinal(byteMi))
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}


