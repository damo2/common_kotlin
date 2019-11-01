package com.weiyao.zuzuapp.fragment

import android.widget.Toast
import com.app.common.json.toJsonExt
import com.damo.libdb.Dao
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.activity.LogShareActivity
import com.weiyao.zuzuapp.activity.test.Test2Activity
import com.weiyao.zuzuapp.activity.test.TestActivity
import com.weiyao.zuzuapp.base.BaseFragment
import com.weiyao.zuzuapp.testmodule.daili.ProxyDemo
import com.weiyao.zuzuapp.testmodule.service.TestJobSchedulerService
import kotlinx.android.synthetic.main.fragment_other.*
import org.jetbrains.anko.startActivity
import kotlin.random.Random


/**
 * Created by wr
 * Date: 2019/6/28  14:27
 * mail: 1902065822@qq.com
 * describe:
 */
class OtherFragment : BaseFragment() {

    data class UserBean(var name: String, var age: Int)

    var userBean by Dao<UserBean>("user",UserBean::class.java)

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
            ProxyDemo.staticProxy()
        }

        btnDynamic.setOnClickListener {
            ProxyDemo.dynamicProxy()
        }
        btnTest2.setOnClickListener {
            mActivity?.startActivity<Test2Activity>()
        }
    }

}