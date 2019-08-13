package com.weiyao.zuzuapp.testmodule.daili.dynamicproxy

/**
 * Created by wr
 * Date: 2019/7/31  19:13
 * mail: 1902065822@qq.com
 * describe:
 */
interface ITrip {
    //买票
    @NameOfClass("buyTrain")
    fun buyTrain()

    //游玩
    fun play(@NameOfParam("playName") name: String, @NameOfParam("playPrice") price: Double, @NameOfParam("startTime") startTime: String, @NameOfParam("endTime") endTime: String)

    //住宿
    fun hotel(@NameOfParam("hotelName") name: String, @NameOfParam("hotelPrice") price: Double)
}