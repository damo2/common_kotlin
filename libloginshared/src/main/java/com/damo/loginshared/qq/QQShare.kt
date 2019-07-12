package com.damo.loginshared.qq

import android.app.Activity
import android.os.Bundle
import com.damo.loginshared.Const
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


/**
 * Created by wr
 * Date: 2018/8/28  21:17
 * describe: qq分享工具类
 */

object QQShare {

    fun shareToQQ(mActivity: Activity, url: String, title: String, description: String,
                  imageUrl: String = Const.SHARE_ICON_APP_URL, onComplete: ((result: Any?) -> Unit)? = null, onError: ((error: UiError?) -> Unit)? = null, onCancel: (() -> Unit)? = null) {
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

        Tencent.createInstance(Const.QQ_APP_ID, mActivity.application).shareToQQ(mActivity, bundle, object : IUiListener {
            override fun onComplete(result: Any?) {
                onComplete?.invoke(result)
            }

            override fun onCancel() {
                onCancel?.invoke()
            }

            override fun onError(error: UiError?) {
                onError?.invoke(error)
            }
        })
    }
}