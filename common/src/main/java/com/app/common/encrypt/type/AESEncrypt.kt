package com.app.common.encrypt.type

import android.util.Base64
import com.app.common.encrypt.IEncrypt
import java.security.Provider
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Created by wangru
 * Date: 2018/7/3  19:00
 * mail: 1902065822@qq.com
 * describe:
 * 快速安全的加密方式。
 */
class AESEncrypt : IEncrypt {
    private val key: String get() = "qwertyuiopasdfgh"

    companion object {
        val CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding" //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
        private val AES = "AES"//AES 加密
        private val SHA1PRNG = "SHA1PRNG"// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    }

    /**
     * 加密
     */
    override fun encrypt(data: String, key: String): String {
        check(data.isNotBlank())
        try {
            val result = encrypt(key, data.toByteArray())
            return String(Base64.encode(result, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }

    /**
     * 解密
     */
    override fun decrypt(data: String, key: String): String {
        check(data.isNotBlank())
        try {
            val enc = Base64.decode(data, Base64.DEFAULT)
            val result = decrypt(key, enc)
            return String(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }


    //加密
    @Throws(Exception::class)
    private fun encrypt(key: String, clear: ByteArray): ByteArray {
        val raw = getRawKey(key.toByteArray())
        //raw必须为128或192或256bits 就是16或24或32byte
        val secretKey = SecretKeySpec(raw, AES)
        val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(clear)
    }


    //解密
    @Throws(Exception::class)
    private fun decrypt(key: String, encrypted: ByteArray): ByteArray {
        val raw = getRawKey(key.toByteArray())
        val secretKey = SecretKeySpec(raw, AES)
        val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(encrypted)
    }

    /*
    * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
    */
    fun generateKey(): String? {
        try {
            val localSecureRandom = SecureRandom.getInstance(SHA1PRNG)
            val bytesKey = ByteArray(20)
            localSecureRandom.nextBytes(bytesKey)
            return bytesToHexString(bytesKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 对密钥进行处理
    @Throws(Exception::class)
    private fun getRawKey(seed: ByteArray): ByteArray {
        val kgen = KeyGenerator.getInstance(AES)
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        val sdkVersion = android.os.Build.VERSION.SDK_INT
        val sr = if (sdkVersion > 23) {  // Android  6.0 以上
            SecureRandom.getInstance(SHA1PRNG, CryptoProvider())
        } else {   //4.2及以上
            SecureRandom.getInstance(SHA1PRNG, "Crypto")
        }
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed)
        kgen.init(128, sr) //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        val skey = kgen.generateKey()
        return skey.encoded
    }

    private fun bytesToHexString(src: ByteArray?): String? {
        return buildString {
            if (src == null || src.isEmpty()) {
                return null
            }
            for (i in 0 until src.size) {
                val v = src[i].toInt() and 0xFF
                val hv = Integer.toHexString(v)
                if (hv.length < 2) {
                    append(0)
                }
                append(hv)
            }
        }
    }

    class CryptoProvider : Provider("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)") {
        /**
         * Creates a Provider and puts parameters
         */
        init {
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl")
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software")
        }
    }
}