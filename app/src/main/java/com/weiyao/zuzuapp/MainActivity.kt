package com.weiyao.zuzuapp

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.common.api.ApiException
import com.app.common.api.RequestFileManager
import com.app.common.api.subscribeExtApi
import com.app.common.api.transformer.composeLife
import com.app.common.api.util.LifeCycleEvent
import com.app.common.extensions.limitLengthExt
import com.app.common.extensions.setOnClickExtNoFast
import com.app.common.json.GsonUtil
import com.app.common.json.toJsonExt
import com.app.common.logger.logd
import com.app.common.utils.SpanUtils
import com.app.common.utils.StorageUtils
import com.app.common.view.toastInfo
import com.damo.libdb.Dao
import com.google.gson.JsonObject
import com.weiyao.zuzuapp.activity.AnkoActivity
import com.weiyao.zuzuapp.activity.test.Test2Activity
import com.weiyao.zuzuapp.activity.test.Test3Activity
import com.weiyao.zuzuapp.activity.test.TestActivity
import com.weiyao.zuzuapp.api.ApiManager
import com.weiyao.zuzuapp.api.abc
import com.weiyao.zuzuapp.api.composeDefault
import com.weiyao.zuzuapp.base.BaseActivity
import com.weiyao.zuzuapp.base.BaseBean
import com.weiyao.zuzuapp.service.TestJobSchedulerService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.io.File
import java.util.*

class MainActivity : BaseActivity() {
    override fun bindLayout(): Int = R.layout.activity_main

    data class UserBean(var name: String, var age: Int)

    var userBean by Dao<UserBean>(UserBean::class.java, "user")


    override fun initData() {
        super.initData()
    }

    override fun initListener() {
        super.initListener()
        tvPutCache.setOnClickListener {
            userBean = UserBean("张三", Random().nextInt(100))
        }
        tvGetCache.setOnClickListener {
            Toast.makeText(applicationContext, userBean.toJsonExt(), Toast.LENGTH_SHORT).show()
        }

        //todo 需要读写权限
        tvDownload.setOnClickListener {
            RequestFileManager.downloadFile(
                    "http://wangru.oss-cn-qingdao.aliyuncs.com/test/erp-v1.0.0-20190404.apk",
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
                    .compose(composeLife(LifeCycleEvent.DESTROY, lifecycleSubject))//结束时取消订阅
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

        tvRN.setOnClickExtNoFast {
            startActivity<MainActivityRN>()
        }

        tvAnko.setOnClickExtNoFast {
            startActivity<AnkoActivity>()
        }
        edtInput.limitLengthExt(5, { toastInfo("最多输入字数为5") })

        tvSpan.text = SpanUtils.generateSideIconText(true, 10, "左边一个图 10px", ContextCompat.getDrawable(mContext, R.drawable.common_loading_icon))

        tvSpan2.text = SpanUtils.generateHorIconText("左右2个图 20px", 20, ContextCompat.getDrawable(mContext, R.drawable.common_toast_info), 20, ContextCompat.getDrawable(mContext, R.drawable.common_toast_error))

        tvService.setOnClickListener {
            TestJobSchedulerService.startJobScheduler(applicationContext)
        }

        tvTest.setOnClickListener {
            startActivity<TestActivity>()
        }

        tvTest2.setOnClickListener {
            startActivity<Test2Activity>()
        }

        tvTest3.setOnClickListener {
            startActivity<Test3Activity>()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }
}
