package com.weiyao.zuzuapp.testmodule.dynamicagent

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * Created by wr
 * Date: 2019/8/1  20:54
 * mail: 1902065822@qq.com
 * describe:
 */
object DynamicProxyDemo {
    fun test() {
        val stationImpl = TrainStation()//将要被代理的真实对象
        /**
         * 动态代理就好处在这里，不管这里是要代理什么对象，I
         * nvocationHandlerImpl与Proxy中代码都不必改变，
         * 都是用下面同样的方式去产生代理对象
         */
        val handler = InvocationHandlerImpl(stationImpl)//用InvocationHandler的实现类包装真实的被代理角色
        val loader = handler.javaClass.classLoader//获取当期那java程序的类装在器Classloadler
        val interfaces = stationImpl.javaClass.interfaces//获取被代理角色实现的所有接口
        /**
         * Proxy类是动态代理模式涉及到的另一个重要的类，该类即为动态代理类，作用类似于静态代理模式中的代理类StationProxy，
         * 它有一个重要方法tatic Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)：返回代理类的一个实例。
         * 其中loader是类装载器，interfaces是真实类所拥有的全部接口的数组，传递此参数以使产生的代理对象可以当做真实类任意实现接口的子类来用，
         * h是调用处理器InvocationHandler。
         */
        val iBuy = Proxy.newProxyInstance(loader, interfaces, handler) as IBuy
        iBuy.buyThing()//将会去执行DynamicProxy的invoke方法，完成对目标对象方法的调用
    }
}
