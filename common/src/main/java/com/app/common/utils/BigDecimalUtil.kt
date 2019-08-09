package com.app.common.utils

import java.math.BigDecimal

/**
 * Created by wr
 * Date: 2019/8/9  13:40
 * mail: 1902065822@qq.com
 * describe:
 * float,double , BigDecimal(double) 计算浮点数的时候,二进制无法精确表示0.1的值(就好比十进制无法精确表示1/3一样),所以一般会对小数格式化处理。
 * 使用BigDecimal(String) 得到精准的结果
 * 详情请看 https://www.cnblogs.com/zouhao/p/6713230.html
 */
object BigDecimalUtil {

    // v1 + v2
    fun add(v1: Number, v2: Number): BigDecimal {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.add(b2)
    }

    // v1 - v2
    fun sub(v1: Number, v2: Number): BigDecimal {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.subtract(b2)
    }

    // v1 * v2
    fun mul(v1: Number, v2: Number): BigDecimal {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.multiply(b2)
    }

    ///scale 默认保留2位小数
    fun div(v1: Number, v2: Number, scale: Int = 2): BigDecimal {
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        // ROUND_HALF_UP = 四舍五入， 应对除不尽的情况
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
    }
}
