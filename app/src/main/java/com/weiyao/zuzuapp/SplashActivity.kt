package com.weiyao.zuzuapp

import com.weiyao.zuzuapp.base.BaseActivity
import org.jetbrains.anko.startActivity

/**
 * Created by wr
 * Date: 2019/9/2  15:19
 * mail: 1902065822@qq.com
 * describe:
 */
class SplashActivity : BaseActivity() {
    override fun bindLayout(): Int = R.layout.main_activity_splash

    override fun initValue() {
        super.initValue()
        startActivity<MainActivity>()
        finish()
    }
}