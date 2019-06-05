package com.weiyao.zuzuapp.activity.test

import com.app.common.logger.logd
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseActivity
import com.weiyao.zuzuapp.base.BaseFragment
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.fragment_test.*


/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class TestActivity : BaseActivity() {
    @State
    var username: String? = null

    var fragment = TestFragment()

    override fun bindLayout(): Int? = R.layout.activity_test


    override fun initData() {
        super.initData()
        initFragment()
        tvUsername.text = username
    }

    override fun initListener() {
        super.initListener()
        tvSetValue.setOnClickListener {
            username = "张三"
            tvUsername.text = username
        }
    }

    private fun initFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentView, fragment)
        transaction.commit()
    }

}

class TestFragment : BaseFragment() {
    @State
    var name: String? = null

    override fun bindLayout(): Int = R.layout.fragment_test

    override fun initData() {
        super.initData()
        tvName.text = name
        logd("name$name")
    }

    override fun initListener() {
        super.initListener()
        btnSetName.setOnClickListener {
            name = "李四"
            tvName.text = name
        }
    }

}