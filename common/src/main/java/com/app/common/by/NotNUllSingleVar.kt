package com.app.common.by

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by wr
 * Date: 2019/4/23  20:01
 * mail: 1902065822@qq.com
 * describe:
 * 只能赋值一次，且不能为空
 */
class NotNullSingle<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("还没有被赋值")
    }
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null&&value!=null) value else throw IllegalStateException("不能设置为null，或已经有值了")
    }
}