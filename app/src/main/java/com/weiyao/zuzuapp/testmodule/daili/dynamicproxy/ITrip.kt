package com.weiyao.zuzuapp.testmodule.daili.dynamicproxy

/**
 * Created by wr
 * Date: 2019/7/31  19:13
 * mail: 1902065822@qq.com
 * describe:
 */
interface ITrip {
    //买票
    fun buyTrain()

    //游玩
    fun play(@Name(value = "playName") name: String,@Name(value = "playPrice") price: Double)

    //住宿
    fun hotel(@Name(value = "hotelName") name: String,@Name(value = "hotelPrice") price: Double)
}