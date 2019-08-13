package com.weiyao.zuzuapp.testmodule.daili

import com.weiyao.zuzuapp.testmodule.daili.dynamicproxy.ITrip
import com.weiyao.zuzuapp.testmodule.daili.dynamicproxy.InvocationHandlerImpl
import com.weiyao.zuzuapp.testmodule.daili.dynamicproxy.ToHankouTrip
import com.weiyao.zuzuapp.testmodule.daili.dynamicproxy.ToShanghaiTrip
import com.weiyao.zuzuapp.testmodule.daili.staticproxy.ToHankouTripStaticProxy
import com.weiyao.zuzuapp.testmodule.daili.staticproxy.ToShanghaiTripStaticProxy
import java.lang.reflect.Proxy

/**
 * Created by wr
 * Date: 2019/8/1  20:54
 * mail: 1902065822@qq.com
 * describe:
 * 动态代理、静态代理demo
 */
object ProxyDemo {
    ///静态代理
    fun staticProxy() {
        val hankouTrip = ToHankouTripStaticProxy("黄鹤楼")
        hankouTrip.buyTrain()
        hankouTrip.play()
        hankouTrip.hotel()

        val shanghaiTrip = ToShanghaiTripStaticProxy("外滩")
        shanghaiTrip.buyTrain()
        shanghaiTrip.play()
        shanghaiTrip.hotel()
    }

    ///动态代理
    fun dynamicProxy() {
        val obj1 = ToHankouTrip("")//将要被代理的真实对象
        dynamicproxyFun(obj1).apply {
            buyTrain()
            play("黄鹤楼", 200.00, "12:00", "15:00")
            hotel("武汉七天连锁酒店", 120.00)
        }

        val obj2 = ToShanghaiTrip("")
        dynamicproxyFun(obj2).apply {
            buyTrain()
            play("外滩", 50.00, "19:00", "20:00")
            hotel("上海如家酒店", 150.00)
        }
    }

    /**
     * 动态代理就好处在这里，不管这里是要代理什么对象，
     * InvocationHandlerImpl与Proxy中代码都不必改变，
     * 都是用下面同样的方式去产生代理对象
     */
    private fun dynamicproxyFun(impl: ITrip): ITrip {
        val handler = InvocationHandlerImpl(impl)//用InvocationHandler的实现类包装真实的被代理角色
        val loader = handler.javaClass.classLoader//获取当期那java程序的类装在器Classloadler
        val interfaces = impl.javaClass.interfaces//获取被代理角色实现的所有接口
        /**
         * Proxy类是动态代理模式涉及到的另一个重要的类，该类即为动态代理类，
         * 它有一个重要方法tatic Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)：返回代理类的一个实例。
         * 其中loader是类装载器，interfaces是真实类所拥有的全部接口的数组，传递此参数以使产生的代理对象可以当做真实类任意实现接口的子类来用，
         * h是调用处理器InvocationHandler。
         */
        return Proxy.newProxyInstance(loader, interfaces, handler) as ITrip
    }
}
