package com.weiyao.zuzuapp.testmodule.daili.staticproxy

import com.app.common.logger.logd

/**
 * Created by wr
 * Date: 2019/7/31  19:20
 * mail: 1902065822@qq.com
 * describe:
 * 代理类
 */
class ToShanghaiTripStaticProxy(private var place: String? = null) : ITripStatic {
    private val trip = ToShanghaiTripStatic(place)

    override fun buyTrain() {
        logd("静态代理 buyTrain 开始")
        trip.buyTrain()
        logd("静态代理 buyTrain 结束")
    }

    override fun play() {
        logd("静态代理 play 开始")
        trip.play()
        logd("静态代理 play 结束")
    }

    override fun hotel() {
        logd("静态代理 hotel 开始")
        trip.hotel()
        logd("静态代理 hotel 结束")
    }

}
