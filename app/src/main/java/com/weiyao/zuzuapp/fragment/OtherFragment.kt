package com.weiyao.zuzuapp.fragment

import android.widget.Toast
import com.app.common.json.toJsonExt
import com.damo.libdb.Dao
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseFragment
import com.weiyao.zuzuapp.service.TestJobSchedulerService
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_other.*
import kotlinx.android.synthetic.main.fragment_other.tvGetCache
import kotlinx.android.synthetic.main.fragment_other.tvPutCache
import kotlinx.android.synthetic.main.fragment_other.tvService
import java.util.*

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
        tvPutCache.setOnClickListener {
            userBean = UserBean("张三", Random().nextInt(100))
        }
        tvGetCache.setOnClickListener {
            Toast.makeText(mContext, userBean.toJsonExt(), Toast.LENGTH_SHORT).show()
        }
        tvService.setOnClickListener {
            TestJobSchedulerService.startJobScheduler(mContext)
        }
    }

}