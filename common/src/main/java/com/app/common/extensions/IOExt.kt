package com.app.common.extensions

import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

/**
 * 写出字符串
 */
fun OutputStream.writer(str: String, charset: Charset = Charsets.UTF_8) = this.writer(charset).write(str)

/**
 * 读取字符串
 */
fun InputStream.readerText(charset: Charset = Charsets.UTF_8): String = this.bufferedReader(charset).readText()
