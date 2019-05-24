package com.app.common.encrypt.type

import com.app.common.encrypt.IEncrypt
import com.app.common.logger.logd

/**
 * Created by wangru
 * Date: 2018/7/3  20:27
 * mail: 1902065822@qq.com
 * describe:
 * 推荐加密
 */

object RSAEncrypt : IEncrypt {

    //公钥加密
    override fun encrypt(data: String, publicKey: String): String = Base64.encode((RSAUtils.encryptByPublicKeyForSpilt(data.toByteArray(), Base64.decode(publicKey))))

    //私钥解密
    override fun decrypt(data: String, privateKey: String): String = String(RSAUtils.decryptByPrivateKeyForSpilt(Base64.decode(data), Base64.decode(privateKey)))


    fun testEncrypt() {
        val data="其算法原理基于很简单的数学知识：既对两个大素数相乘得到其乘积很简单，但对乘积进行因数分解很难，两个大素数组合即为公钥，乘积未秘钥。只要保证两个不想等的素数足够大就可以保证加密足够安全。"
        //生成密钥对
        val keyPair = RSAUtils.generateRSAKeyPair(RSAUtils.DEFAULT_KEY_SIZE)!!
        //获取公钥
        val publicKey = RSAUtils.getPublicKey(keyPair)
        //获取私钥
        val privateKey = RSAUtils.getPrivateKey(keyPair)
        logd("公钥:$publicKey\n 私钥:$publicKey")
        //用公钥加密
        val encrypt1 = RSAEncrypt.encrypt(data, publicKey)
        logd("加密后：\n$encrypt1");
        //用私钥解密
        val decrypt1 = RSAEncrypt.decrypt(encrypt1, privateKey)
        logd("解密后：\n$decrypt1" );
    }
}


