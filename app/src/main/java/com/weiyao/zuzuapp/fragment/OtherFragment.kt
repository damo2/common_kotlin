package com.weiyao.zuzuapp.fragment

import android.widget.Toast
import com.app.common.json.toJsonExt
import com.damo.libdb.Dao
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.activity.LogShareActivity
import com.weiyao.zuzuapp.activity.test.Test2Activity
import com.weiyao.zuzuapp.activity.test.TestActivity
import com.weiyao.zuzuapp.base.BaseFragment
import com.weiyao.zuzuapp.service.TestJobSchedulerService
import com.weiyao.zuzuapp.testmodule.dynamicagent.IBuy
import com.weiyao.zuzuapp.testmodule.dynamicagent.InvocationHandlerImpl
import com.weiyao.zuzuapp.testmodule.dynamicagent.TrainStation
import com.weiyao.zuzuapp.testmodule.staticagent.Huangniu
import kotlinx.android.synthetic.main.fragment_other.*
import org.jetbrains.anko.startActivity
import java.lang.reflect.Proxy
import kotlin.random.Random


/**
 * Created by wr
 * Date: 2019/6/28  14:27
 * mail: 1902065822@qq.com
 * describe:
 */
class OtherFragment : BaseFragment() {

    data class UserBean(var name: String, var age: Int)

    var userBean by Dao<UserBean>(UserBean::class.java, "user")

    override fun bindLayout() = R.layout.fragment_other

    override fun initListener() {
        super.initListener()
        btnPutCache.setOnClickListener {
            userBean = UserBean("张三", Random.nextInt(100))
        }
        btnGetCache.setOnClickListener {
            Toast.makeText(mContext, userBean.toJsonExt(), Toast.LENGTH_SHORT).show()
        }
        btnService.setOnClickListener {
            TestJobSchedulerService.startJobScheduler(mContext)
        }
        btnLoginShare.setOnClickListener {
            mActivity?.startActivity<LogShareActivity>()
        }
        btnTest.setOnClickListener {
            mActivity?.startActivity<TestActivity>()
        }

        btnStatic.setOnClickListener {
            val iBuy = Huangniu("武汉")
            iBuy.buyThing()
        }

        btnDynamic.setOnClickListener {
            val stationImpl = TrainStation()//将要被代理的真实对象
            val handler = InvocationHandlerImpl(stationImpl)//用InvocationHandler的实现类包装真实的被代理角色
            val loader = handler.javaClass.classLoader//获取当期那java程序的类装在器Classloadler
            val interfaces = stationImpl.javaClass.interfaces//获取被代理角色实现的所有接口
            val iBuy = Proxy.newProxyInstance(loader, interfaces, handler) as IBuy
            iBuy.buyThing()//将会去执行DynamicProxy的invoke方法，完成对目标对象方法的调用
        }
        btnTest2.setOnClickListener {
            mActivity?.startActivity<Test2Activity>()
        }
    }

}