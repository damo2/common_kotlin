package com.weijian.monitor.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.common.base.AppBaseDialogFragment

/**
 * Created by wangru
 * Date: 2018/12/18  13:45
 * mail: 1902065822@qq.com
 * describe:
 */
abstract class BaseDialogFragment(private var isInject: Boolean = false) : AppBaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}