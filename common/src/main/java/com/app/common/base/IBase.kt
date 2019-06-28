package com.app.common.base

import android.content.Context
import com.app.common.api.util.LifeCycleEvent
import io.reactivex.subjects.PublishSubject

/**
 * Created by wangru
 * Date: 2018/7/5  18:14
 * mail: 1902065822@qq.com
 * describe:
 */

interface IBase {
    fun initTop()
    fun initData()
    fun initView()
    fun initValue()
    fun initListener()
    fun getLifecycleSubject(): PublishSubject<LifeCycleEvent>
    fun getMyContext():Context
}
