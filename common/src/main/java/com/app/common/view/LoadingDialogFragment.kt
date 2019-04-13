package com.app.common.view

import android.view.Gravity
import com.app.common.R
import com.app.common.base.AppBaseDialogFragment


/**
 * Created by wr
 * Date: 2018/8/28  20:03
 * describe:
 */
class LoadingDialogFragment : AppBaseDialogFragment() {

    override fun bindLayout(): Int = R.layout.common_dialog_loading
    override fun initTop() {
        super.initTop()
        dialog.window?.attributes?.gravity = Gravity.CENTER
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onStart() {
        super.onStart()
        setOutBackgroundTransparent()
    }
}