package com.app.common.file;

import android.support.annotation.NonNull

/**
 * 获取和判断文件头信息
 *
 * @author Sud
 *
 */
object HexKt {
    /**
     * @param bytes
     * @return 进制字符串
     */
    fun bytesToHexString(bytes: ByteArray?): String? {
        kotlin.run {
            var result: String? = null
            if (bytes != null && bytes.isNotEmpty()) {
                val stringBuilder = StringBuilder("")
                for (i in 0 until bytes.size) {
                    val v = bytes[i].toInt() and 0xFF
                    val hv = Integer.toHexString(v).toUpperCase()
                    if (hv.length < 2) {
                        stringBuilder.append(0)
                    }
                    stringBuilder.append(hv)
                }
                result = stringBuilder.toString()
            }
            return result
        }
    }

    /**
     * 十六进制String转换成Byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    fun hexStringToBytes(@NonNull hexString: String): ByteArray? {

        val local = hexString.toUpperCase()
        val length = local.length / 2
        val hexChars = local.toCharArray()
        val d = ByteArray(length)
        for (i in 0 until length) {
            val pos = i * 2
            d[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
        }
        return d
    }

    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }
}