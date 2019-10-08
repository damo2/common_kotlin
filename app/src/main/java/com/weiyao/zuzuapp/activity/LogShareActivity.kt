package com.weiyao.zuzuapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.app.common.extensions.toBitmapExt
import com.app.common.json.GsonUtil
import com.app.common.view.toastInfo
import com.damo.loginshared.qq.QQLogin
import com.damo.loginshared.qq.QQShare
import com.damo.loginshared.sina.SinaLogin
import com.damo.loginshared.sina.SinaShare
import com.damo.loginshared.wechat.WechatLogin
import com.damo.loginshared.wechat.WechatShare
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_logshare.*


/**
 * Created by wr
 * Date: 2019/5/7  11:10
 * mail: 1902065822@qq.com
 * describe:
 */
class LogShareActivity : BaseActivity() {

    private val qqLogin = QQLogin()
    private val weiboLogin = SinaLogin()
    override fun bindLayout(): Int? = R.layout.activity_logshare


    override fun initData() {
        super.initData()
    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        super.initListener()
        btnQQLogin.setOnClickListener {
            qqLogin.login(this, { isSuc, qqDataBean ->
                if (isSuc) {
                    toastInfo("授权成功")
                    tvInfo.text = GsonUtil().toJson(qqDataBean)
                }
                toastInfo("授权${if (isSuc) "成功" else "失败"}")
            }, { isSuc, qqUserInfoBean, errorInfo ->
                if (isSuc) {
                    tvInfo.text = "QQ用户信息：\n${GsonUtil().toJson(qqUserInfoBean)}"
                }
                toastInfo("获取用户信息${if (isSuc) "成功" else "授权失败"}")
            })
        }

        btnWeixinLogin.setOnClickListener {
            WechatLogin(mContext).login()
        }

        btnWeiboLogin.setOnClickListener {
            weiboLogin.login(this@LogShareActivity, { isSuc, errorInfo, accessToken ->
                toastInfo("授权${if (isSuc) "成功" else "失败"}")

            }, { isSuc, userBean ->
                if (isSuc) {
                    tvInfo.text = "微博用户信息:\n${GsonUtil().toJson(userBean)}"
                }
                toastInfo("获取用户信息${if (isSuc) "成功" else "授权失败"}")
            })
        }

        btnQQShare.setOnClickListener {
            QQShare.shareToQQ(
                    this,
                    "http://www.baidu.com",
                    "百度",
                    "百度一下，你就知道",
                    "https://www.baidu.com/img/baidu_resultlogo@2.png",
                    onComplete = {

                    },
                    onError = {

                    },
                    onCancel = {

                    })
        }

        btnWeixinShare.setOnClickListener {
            WechatShare.shareToWechat(mContext, WechatShare.WX_FRIEND, "http://www.baidu.com", "百度", "百度一下，你就知道", getDrawable(R.drawable.ic_app).toBitmapExt())
        }

        btnWeiboShare.setOnClickListener {
            SinaShare.shareToWeibo(this, "百度", "百度一下，你就知道", getDrawable(R.drawable.ic_app).toBitmapExt())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        qqLogin.onActivityResult(requestCode, resultCode, data)
        weiboLogin.onActivityResult(requestCode, resultCode, data)
    }

}
