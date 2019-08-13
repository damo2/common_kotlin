package com.weiyao.zuzuapp.testmodule.daili.dynamicproxy

/**
 * Created by wr
 * Date: 2019/8/13  15:45
 * mail: 1902065822@qq.com
 * describe:
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class NameOfParam(val value: String)

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class NameOfClass(val value: String)

