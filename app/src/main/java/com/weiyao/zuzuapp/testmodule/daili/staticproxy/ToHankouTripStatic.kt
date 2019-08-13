package com.weiyao.zuzuapp.testmodule.daili.staticproxy

import com.app.common.logger.logd

/**
 * Created by wr
 * Date: 2019/7/31  19:17
 * mail: 1902065822@qq.com
 * describe:
 * 被代理类
 */
class ToHankouTripStatic(private var place: String? = null) : ITripStatic {
    override fun buyTrain() {
        logd("买去汉口的火车票")
    }

    override fun play() {
        logd("去${place}游玩")
    }

    override fun hotel() {
        logd("晚上住汉口的酒店")
    }
}