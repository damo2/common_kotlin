package com.damo.test.activity.test

import android.os.Bundle
import com.app.common.view.toastInfo
import com.damo.test.R
import com.damo.test.base.BaseActivity
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class TestActivity : BaseActivity() {
    @State
    var username: String? = null

    override fun bindLayout(): Int? = R.layout.activity_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toastInfo(username)
    }

    override fun initListener() {
        super.initListener()
        tvSetValue.setOnClickListener {
            username = "张三"
        }
        tvGetValue.setOnClickListener {
            //横竖屏切换后看toast
            toastInfo(username)
        }
    }

}