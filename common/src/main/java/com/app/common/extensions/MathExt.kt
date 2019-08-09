package com.app.common.extensions

import com.app.common.utils.BigDecimalUtil

/**
 * Created by wr
 * Date: 2019/8/9  15:08
 * mail: 1902065822@qq.com
 * 加减乘除精确算法
 *
 *
 * describe: infix 中缀函数
 * 使用：val a= 1.01 add 1.02  或 val a= 1.01.add(1.02)
 */
// v1 + v2
infix fun Number.add(num: Number) = BigDecimalUtil.add(this, num)

// v1 - v2
infix fun Number.sub(num: Number) = BigDecimalUtil.sub(this, num)

// v1 * v2
infix fun Number.mul(num: Number) = BigDecimalUtil.mul(this, num)

// v1 / v2
fun Number.div(num: Number, scale: Int = 2) = BigDecimalUtil.div(this, num, scale)

infix fun Number.div(num: Number) = BigDecimalUtil.div(this, num)