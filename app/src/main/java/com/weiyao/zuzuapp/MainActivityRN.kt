package com.weiyao.zuzuapp

import android.os.Bundle
import com.weiyao.zuzuapp.BuildConfig
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseActivity
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView



class MainActivityRN : BaseActivity() {
    override fun bindLayout(): Int = R.layout.activity_main

    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mReactRootView = ReactRootView(this)
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                //这里需要注意，官方文档setJSMainModuleName在新  版本中找不到，替换为setJSMainModulePath
                //                .setJSMainModuleName("index.android")
                //这里的路径是相对于根目录的，填入index.android即可
                .setJSMainModulePath("index.android")
                .addPackage(MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()
        //注意这里的`moduleName`参数必须和工程名字，也是就是在`index.android.js`中AppRegistry.registerComponent（）注册的名字一样
        mReactRootView!!.startReactApplication(mReactInstanceManager, "damo", null)

        setContentView(mReactRootView)
    }

    fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
