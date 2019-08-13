package com.weiyao.zuzuapp.testmodule.daili.staticproxy

/**
 * Created by wr
 * Date: 2019/7/31  19:13
 * mail: 1902065822@qq.com
 * describe:
 * 1、定义一个接口，声明被代理的类所需要实现的功能
 */
interface ITripStatic {
    //买票
    fun buyTrain()
    //游玩
    fun play()
    //住宿
    fun hotel()
}