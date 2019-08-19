package com.app.common.encrypt.type

import android.os.Build
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


/**
 * Created by wr
 * Date: 2019/5/23  20:38
 * mail: 1902065822@qq.com
 * describe:
 */
object RSAUtils {
    private val RSA = "RSA"// 非对称加密密钥算法
    private val ECB_PKCS1_PADDING = "RSA/ECB/OAEPWithSHA256AndMGF1Padding"//加密填充方式
    private val DEFAULT_KEY_SIZE = 1024//秘钥默认长度
    private val DEFAULT_SPLIT = "#PART#".toByteArray()    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    private val DEFAULT_BUFFERSIZE = DEFAULT_KEY_SIZE / 8 - 11// 当前秘钥支持加密的最大字节数

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048
     * 一般1024
     * @return
     */
    fun generateRSAKeyPair(keyLength: Int = DEFAULT_KEY_SIZE): KeyPair? {
        try {
            val kpg = KeyPairGenerator.getInstance(RSA)
            kpg.initialize(keyLength)
            return kpg.genKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

    }

    // 获取公钥
    fun getPublicKey(keyPair: KeyPair): String {
        val rsaPublicKey = keyPair.public as RSAPublicKey
        return Base64.encode(rsaPublicKey.encoded)
    }

    //获取私钥
    fun getPrivateKey(keyPair: KeyPair): String {
        val rsaPrivateKey = keyPair.private as RSAPrivateKey
        return Base64.encode(rsaPrivateKey.encoded)
    }

    /**
     * 获取数字签名
     *
     * @param data       二进制位
     * @param privateKey 私钥(BASE64编码)
     * @return 数字签名结果字符串
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    fun sign(data: ByteArray, privateKey: String): String {
        val keyBytes = Base64.decode(privateKey)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = getKeyFactory()
        val privateK = keyFactory.generatePrivate(pkcs8KeySpec)

        val signature = Signature.getInstance("MD5withRSA")
        signature.initSign(privateK)
        signature.update(data)
        return Base64.encode(signature.sign())
    }

    /**
     * 数字签名校验
     *
     * @param data      二进位组
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名字符串
     * @return true：校验成功，false：校验失败
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    fun verify(data: ByteArray, publicKey: String, sign: String): Boolean {
        val keyBytes = Base64.decode(publicKey)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = getKeyFactory()
        val publicK = keyFactory.generatePublic(keySpec)

        val signature = Signature.getInstance("MD5withRSA")
        signature.initVerify(publicK)
        signature.update(data)
        return signature.verify(Base64.decode(sign))
    }

    /**
     * 获取 KeyFactory
     *
     * @throws NoSuchAlgorithmException 异常
     */
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class)
    private fun getKeyFactory(): KeyFactory {
        return if (Build.VERSION.SDK_INT >= 16) {
            KeyFactory.getInstance("RSA", "BC")
        } else {
            KeyFactory.getInstance("RSA")
        }
    }


    /**
     * 用公钥对字符串进行分段加密
     */
    @Throws(Exception::class)
    fun encryptByPublicKeyForSpilt(data: ByteArray, publicKey: ByteArray): ByteArray {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey)
        }
        val allBytes = ArrayList<Byte>(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                val encryptBytes = encryptByPublicKey(buf, publicKey)
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                if (i == dataLen - 1) {
                    buf = null
                } else {
                    buf = ByteArray(Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     * 分段加密
     *
     * @param data       要加密的原始数据
     * @param privateKey 秘钥
     */
    @Throws(Exception::class)
    fun encryptByPrivateKeyForSpilt(data: ByteArray, privateKey: ByteArray): ByteArray {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPrivateKey(data, privateKey)
        }
        val allBytes = ArrayList<Byte>(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                val encryptBytes = encryptByPrivateKey(buf, privateKey)
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                if (i == dataLen - 1) {
                    buf = null
                } else {
                    buf = ByteArray(Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     * 公钥分段解密
     *
     * @param encrypted 待解密数据
     * @param publicKey 密钥
     */
    @Throws(Exception::class)
    fun decryptByPublicKeyForSpilt(encrypted: ByteArray, publicKey: ByteArray): ByteArray {
        val splitLen = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPublicKey(encrypted, publicKey)
        }
        val dataLen = encrypted.size
        val allBytes = ArrayList<Byte>(1024)
        var latestStartIndex = 0
        run {
            var i = 0
            while (i < dataLen) {
                val bt = encrypted[i]
                var isMatchSplit = false
                if (i == dataLen - 1) {
                    // 到data的最后了
                    val part = ByteArray(dataLen - latestStartIndex)
                    System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                    val decryptPart = decryptByPublicKey(part, publicKey)
                    for (b in decryptPart) {
                        allBytes.add(b)
                    }
                    latestStartIndex = i + splitLen
                    i = latestStartIndex - 1
                } else if (bt == DEFAULT_SPLIT[0]) {
                    // 这个是以split[0]开头
                    if (splitLen > 1) {
                        if (i + splitLen < dataLen) {
                            // 没有超出data的范围
                            for (j in 1 until splitLen) {
                                if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                    break
                                }
                                if (j == splitLen - 1) {
                                    // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                    isMatchSplit = true
                                }
                            }
                        }
                    } else {
                        // split只有一位，则已经匹配了
                        isMatchSplit = true
                    }
                }
                if (isMatchSplit) {
                    val part = ByteArray(i - latestStartIndex)
                    System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                    val decryptPart = decryptByPublicKey(part, publicKey)
                    for (b in decryptPart) {
                        allBytes.add(b)
                    }
                    latestStartIndex = i + splitLen
                    i = latestStartIndex - 1
                }
                i++
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     * 使用私钥分段解密
     */
    @Throws(Exception::class)
    fun decryptByPrivateKeyForSpilt(encrypted: ByteArray, privateKey: ByteArray): ByteArray {
        val splitLen = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPrivateKey(encrypted, privateKey)
        }
        val dataLen = encrypted.size
        val allBytes = ArrayList<Byte>(1024)
        var latestStartIndex = 0
        run {
            var i = 0
            while (i < dataLen) {
                val bt = encrypted[i]
                var isMatchSplit = false
                if (i == dataLen - 1) {
                    // 到data的最后了
                    val part = ByteArray(dataLen - latestStartIndex)
                    System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                    val decryptPart = decryptByPrivateKey(part, privateKey)
                    for (b in decryptPart) {
                        allBytes.add(b)
                    }
                    latestStartIndex = i + splitLen
                    i = latestStartIndex - 1
                } else if (bt == DEFAULT_SPLIT[0]) {
                    // 这个是以split[0]开头
                    if (splitLen > 1) {
                        if (i + splitLen < dataLen) {
                            // 没有超出data的范围
                            for (j in 1 until splitLen) {
                                if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                    break
                                }
                                if (j == splitLen - 1) {
                                    // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                    isMatchSplit = true
                                }
                            }
                        }
                    } else {
                        // split只有一位，则已经匹配了
                        isMatchSplit = true
                    }
                }
                if (isMatchSplit) {
                    val part = ByteArray(i - latestStartIndex)
                    System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                    val decryptPart = decryptByPrivateKey(part, privateKey)
                    for (b in decryptPart) {
                        allBytes.add(b)
                    }
                    latestStartIndex = i + splitLen
                    i = latestStartIndex - 1
                }
                i++
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }


    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    @Throws(Exception::class)
    private fun encryptByPublicKey(data: ByteArray, publicKey: ByteArray): ByteArray {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPublic = kf.generatePublic(keySpec)
        // 加密数据
        val cp = Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.ENCRYPT_MODE, keyPublic)
        return cp.doFinal(data)
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    @Throws(Exception::class)
    private fun encryptByPrivateKey(data: ByteArray, privateKey: ByteArray): ByteArray {
        // 得到私钥
        val keySpec = PKCS8EncodedKeySpec(privateKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPrivate = kf.generatePrivate(keySpec)
        // 数据加密
        val cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate)
        return cipher.doFinal(data)
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[] 解密数据
     */
    @Throws(Exception::class)
    private fun decryptByPublicKey(data: ByteArray, publicKey: ByteArray): ByteArray {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPublic = kf.generatePublic(keySpec)
        // 数据解密
        val cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, keyPublic)
        return cipher.doFinal(data)
    }

    /**
     * 使用私钥进行解密
     */
    @Throws(Exception::class)
    private fun decryptByPrivateKey(encrypted: ByteArray, privateKey: ByteArray): ByteArray {
        // 得到私钥
        val keySpec = PKCS8EncodedKeySpec(privateKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPrivate = kf.generatePrivate(keySpec)

        // 解密数据
        val cp = Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.DECRYPT_MODE, keyPrivate)
        return cp.doFinal(encrypted)
    }

}