package com.app.common.by

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * 弱引用
 * Created by wangru
 * Date: 2018/7/16  20:52
 * mail: 1902065822@qq.com
 * describe:
 * var activity:Activity ? by Weak()
 *
 */
class Weak<T : Any>(initializer: () -> T?) {
    var mWeakReference = WeakReference<T?>(initializer())

    constructor() : this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return mWeakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        mWeakReference = WeakReference(value)
    }

}