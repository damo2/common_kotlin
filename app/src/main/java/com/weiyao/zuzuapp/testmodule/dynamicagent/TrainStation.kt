package com.weiyao.zuzuapp.testmodule.dynamicagent

import com.app.common.logger.logd

/**
 * Created by wr
 * Date: 2019/7/31  19:17
 * mail: 1902065822@qq.com
 * describe:
 * 2、被代理类
 */
class TrainStation(private var destination: String? = null) : IBuy {
    override fun buyThing() {
        logd("买去${destination}的火车票")
    }
}