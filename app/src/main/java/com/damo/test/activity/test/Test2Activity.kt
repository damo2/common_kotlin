package com.damo.test.activity.test

import com.app.common.update.UpdateApkUtil
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
            UpdateApkUtil.updateInstallApk(mContext,"https://erpwanban.oss-cn-shanghai.aliyuncs.com/apk/wanbanerp_1.0.1_20190416.apk")
        }
    }
}
