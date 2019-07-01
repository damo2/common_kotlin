package com.weiyao.zuzuapp.fragment

import com.app.common.api.ApiException
import com.app.common.api.RequestFileManager
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeLife
import com.app.common.api.util.LifeCycleEvent
import com.app.common.logger.logd
import com.app.common.utils.StorageUtils
import com.app.common.view.toastInfo
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.api.ApiManager
import com.weiyao.zuzuapp.api.composeDefault
import com.weiyao.zuzuapp.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.File

/**
 * Created by wr
 * Date: 2019/6/28  14:27
 * mail: 1902065822@qq.com
 * describe:
 */
class ApiFragment : BaseFragment() {
    override fun bindLayout() = R.layout.fragment_apirequest

    override fun initListener() {
        super.initListener()

        //todo 需要读写权限
        tvDownload.setOnClickListener {
            RequestFileManager.downloadFile(
                    "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk",
                    StorageUtils.getPublicStoragePath("test/wanban.apk"),
                    { file -> toastInfo("下载成功") },
                    { e -> toastInfo("下载失败${e.message}") },
                    { totalLength, contentLength, done ->
                        logd("totalLength=$totalLength contentLength=$contentLength")
                    });
        }

        tvUp.setOnClickListener {
            RequestFileManager.uploadFileByKey(
                    "http://www.wxjishu.com:9999/file/upload",
                    "file",
                    File(StorageUtils.getPublicStoragePath("test/wanban.apk")),
                    { str -> toastInfo("上传成功$str") },
                    { e -> toastInfo("上传失败$e") },
                    { progress, total -> logd("up=$progress total=$total") }
            )
        }

        tvRequest.setOnClickListener {
            ApiManager.apiService
                    .update("1.0")
                    .composeLife(getLifecycleSubject(), LifeCycleEvent.DESTROY)//结束时取消订阅
                    .composeDefault()//统一处理异常，请求后台异常throw ApiException ，异常信息为后台给的异常内容
                    .subscribeExtApi({
                        //成功返回
                        toastInfo(it.toString())
                    }, { e ->
                        //异常，不传默认toast ApiError的异常信息，添加此处理了 isToast 无效。
                        if (e is ApiException) {
                            //可根据后台错误码处理不同异常
                            logd("${e.code}")
                        }
                        toastInfo("更新失败")
                    }, {
                        //请求完成
                    }, isShowLoad = true,// 是否显示进度框
                            context = activity,//isShowLoad 为true时必传
                            isToast = true//是否toast异常，处理了异常时无效
                    )
        }
    }
}