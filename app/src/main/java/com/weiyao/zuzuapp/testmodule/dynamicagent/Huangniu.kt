package com.weiyao.zuzuapp.testmodule.dynamicagent

import com.app.common.logger.logd

/**
 * Created by wr
 * Date: 2019/7/31  19:20
 * mail: 1902065822@qq.com
 * describe:
 * 3、代理类
 */
class Huangniu(private var destination: String? = null) : IBuy {
    override fun buyThing() {
        val trainStation = TrainStation(destination)
        trainStation.buyThing()
        logd("通过黄牛买到了去${destination}的火车票");
    }
}
