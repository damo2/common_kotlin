package com.weiyao.zuzuapp.activity.test

import android.content.Intent
import com.app.common.json.GsonUtil
import com.app.common.logger.logd
import com.app.common.view.toastInfo
import com.damo.loginshared.QQLogin
import com.damo.loginshared.SinaLogin
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test3.*


/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class Test3Activity : BaseActivity() {

    private val qqLogin = QQLogin(this)
    private val weiboLogin = SinaLogin()

    override fun bindLayout(): Int? = R.layout.activity_test3


    override fun initData() {
        super.initData()

    }

    override fun initListener() {
        super.initListener()
        btnQQLogin.setOnClickListener {
            qqLogin.login({ isSuc, qqDataBean ->
                if (isSuc) {
                    toastInfo("授权成功")
                    logd(GsonUtil().toJson(qqDataBean))
                }
                toastInfo("授权${if (isSuc) "成功" else "失败"}")
            }, { isSuc, qqUserInfoBean, errorInfo ->
                if (isSuc) {
                    logd(GsonUtil().toJson(qqUserInfoBean))
                }
                toastInfo("获取用户信息${if (isSuc) "成功" else "授权失败"}")
            })

            btnWeiboLogin.setOnClickListener {
                weiboLogin.login(this, { isSuc, errorInfo, accessToken ->
                    toastInfo("授权${if (isSuc) "成功" else "失败"}")
                }, { isSuc, userBean ->
                    toastInfo("获取用户信息${if (isSuc) "成功" else "授权失败"}")
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        qqLogin.onActivityResult(requestCode, resultCode, data)
        weiboLogin.onActivityResult(requestCode, resultCode, data)
    }

}
