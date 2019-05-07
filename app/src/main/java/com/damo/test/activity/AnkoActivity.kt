package com.damo.test.activity

import android.os.Bundle
import com.damo.test.activity.anko.ActivityAnkoUI
import com.damo.test.base.BaseActivity
import org.jetbrains.anko.setContentView

/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class AnkoActivity : BaseActivity(){
    override fun bindLayout(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityAnkoUI().setContentView(this);
    }
}