package com.app.common.utils

/**
 * Created by wr
 * Date: 2019/2/14  16:56
 * mail: 1902065822@qq.com
 * describe: 带参数的单例封装，双重检查锁定,不需要参数时，只需使用lazy的属性委托
 */

/**
 * 不带参数
 * @param T 单例对象
 */
open class SingleHolder<out T>(private var creator: () -> T) {
    //volatile不保证原子操作，所以，很容易读到脏数据。在两个或者更多的线程访问的成员变量上使用volatile
    @Volatile
    private var instance: T? = null

    fun getInstance(): T =
            instance ?: synchronized(this) {
                instance ?: creator().apply { instance = this }
            }
}

/**
 * 带1个参数
 * @param T 单例对象
 * @param A 参数
 */
open class SingleHolder1<out T, in A>(private var creator: (A) -> T) {
    //volatile不保证原子操作，所以，很容易读到脏数据。在两个或者更多的线程访问的成员变量上使用volatile
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T =
            instance ?: synchronized(this) {
                instance ?: creator(arg).apply { instance = this }
            }
}

/**
 * 带2个参数
 * @param T 单例对象
 * @param A 参数
 */
open class SingleHolder2<out T, in A, in B>(private var creator: (A, B) -> T) {
    //volatile不保证原子操作，所以，很容易读到脏数据。在两个或者更多的线程访问的成员变量上使用volatile
    @Volatile
    private var instance: T? = null

    fun getInstance(argA: A, argB: B): T =
            instance ?: synchronized(this) {
                instance ?: creator(argA, argB).apply { instance = this }
            }
}


