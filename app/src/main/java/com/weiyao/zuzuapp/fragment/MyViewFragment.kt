package com.weiyao.zuzuapp.fragment

import com.app.common.extensions.limitLengthExt
import com.app.common.view.toastInfo
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_myview.*

/**
 * Created by wr
 * Date: 2019/6/28  14:27
 * mail: 1902065822@qq.com
 * describe:
 */
class MyViewFragment : BaseFragment() {
    override fun bindLayout() = R.layout.fragment_myview

    override fun initView() {
        super.initView()
        edtInput.limitLengthExt(5) { toastInfo("最多输入字数为5") }
    }
}