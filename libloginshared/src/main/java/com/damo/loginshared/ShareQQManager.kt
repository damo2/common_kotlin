package com.damo.loginshared

import android.app.Activity
import android.os.Bundle
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


/**
 * Created by wr
 * Date: 2018/8/28  21:17
 * describe: qq分享工具类
 */

object ShareQQManager : IUiListener {
    private val mQQApiId = Const.QQ_APP_ID
    private lateinit var mTencent: Tencent

    fun shareToQQ(mActivity: Activity, url: String, title: String, description: String,
                  imageUrl: String = Const.SHARE_ICON_APP_URL) {
        val bundle = Bundle()
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        // 标题
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        // 摘要
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description)
        // 内容地址
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url)
        // 网络图片地址
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
        // 应用名称
//        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称")
//        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能")

        mTencent = Tencent.createInstance(mQQApiId, mActivity.application)
        mTencent.shareToQQ(mActivity, bundle, this)
    }

    override fun onComplete(p0: Any?) {
//        App.toast.success(p0.toString())
    }

    override fun onCancel() {
//        App.toast.error("取消")
    }

    override fun onError(p0: UiError?) {

    }
}