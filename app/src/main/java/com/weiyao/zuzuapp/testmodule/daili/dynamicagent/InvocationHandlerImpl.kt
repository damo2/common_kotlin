package com.weiyao.zuzuapp.testmodule.daili.dynamicagent

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Created by wr
 * Date: 2019/8/1  20:50
 * mail: 1902065822@qq.com
 * describe:
 */
internal class InvocationHandlerImpl(var proxyobj: IBuy) : InvocationHandler {

    @Throws(Throwable::class)
    override fun invoke(obj: Any, method: Method, args: Array<Any>?): Any? {
        //调用被代理对象proxyobj的方法method,传入一个参数组args
        return method.invoke(proxyobj, *args
                ?: (arrayOf()))
    }
}