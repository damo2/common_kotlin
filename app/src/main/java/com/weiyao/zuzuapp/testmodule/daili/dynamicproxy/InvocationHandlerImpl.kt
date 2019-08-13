package com.weiyao.zuzuapp.testmodule.daili.dynamicproxy

import com.app.common.json.toJsonExt
import com.app.common.logger.logd
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method


/**
 * Created by wr
 * Date: 2019/8/1  20:50
 * mail: 1902065822@qq.com
 * describe:
 * 动态代理类
 */
internal class InvocationHandlerImpl(var proxyobj: ITrip) : InvocationHandler {

    @Throws(Throwable::class)
    override fun invoke(obj: Any, method: Method, args: Array<Any>?): Any? {
        logd("动态代理开始")

        val argsStr = args?.map {
            "$it"
        }?.toList()?.toJsonExt() ?: "null"

        logd("obj interface=${obj.javaClass.interfaces[0]} method=${method.name} args:$argsStr")

        //方法注解
        var argsIndex = 0
        method.parameterAnnotations.forEach { annotations ->
            //方法上参数对应的注解
            annotations?.forEach { annotation ->
                //参数注解类型
                if (annotation is Name) {
                    val name = annotation.value
                    logd("name=$name value=${args?.get(argsIndex)}")
                }
                argsIndex++
            }
        }

        //调用被代理对象proxyobj的方法method,传入一个参数组args
        val argsObj = method.invoke(proxyobj, *args
                ?: (arrayOf()))
        logd("动态代理结束")
        return argsObj
    }
}