package com.damo.test.activity.test

import android.content.Intent
import com.app.common.update.UpdateInstallApk
import com.damo.test.R
import com.damo.test.base.BaseActivity
import com.evernote.android.state.State
import kotlinx.android.synthetic.main.activity_test2.*


/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class Test2Activity : BaseActivity() {
    @State
    var username: String? = null

    var fragment = TestFragment()

    override fun bindLayout(): Int? = R.layout.activity_test2

    override fun initListener() {
        super.initListener()
        btnUpdate.setOnClickListener {
            UpdateInstallApk.getInstance().updateInstallApk(activity, "https://erpwanban.oss-cn-shanghai.aliyuncs.com/apk/wanbanerp_1.0.1_20190416.apk", listener = { addSubscription(it) })

            Test.showInNotificationBar(application, "test", "aaaa", null, 11010, Intent(application, TestActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        UpdateInstallApk.getInstance().onActivityResult(requestCode, resultCode, activity)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
