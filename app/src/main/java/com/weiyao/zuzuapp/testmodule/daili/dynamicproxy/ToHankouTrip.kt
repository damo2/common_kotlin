package com.weiyao.zuzuapp.testmodule.daili.dynamicproxy

import com.app.common.logger.logd

/**
 * Created by wr
 * Date: 2019/7/31  19:17
 * mail: 1902065822@qq.com
 * describe:
 * 2、被代理类
 */
class ToHankouTrip(private var place: String? = null) : ITrip {
    override fun buyTrain() {
        logd("买去汉口的火车票")
    }

    override fun play(name: String, price: Double, startTime: String, endTime: String) {
        logd("在汉口去${name}游玩，花费${price}元，$startTime - $endTime")
    }

    override fun hotel(name: String, price: Double) {
        logd("在汉口住酒店:$name，花费${price}元")
    }
}