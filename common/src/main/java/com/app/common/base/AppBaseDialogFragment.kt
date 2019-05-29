package com.app.common.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.app.common.R
import com.app.common.logger.Logger


/**
 * Created by wr
 * Date: 2018/8/28  19:53
 * describe:
 */
abstract class AppBaseDialogFragment : DialogFragment() {
    protected lateinit var mRootView: View
    protected var mActivity: Activity? = null
    protected var mIsPrepare = false
    protected var mIsBackCancelable = true//返回能取消

    protected val mTag = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置style
        setStyle(STYLE_NORMAL, R.style.BaseDialogFragment)

//        dialog?.setCanceledOnTouchOutside(false)//设置点击外部不可取消
//        isCancelable = false //设置不可取消
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //去除标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        initTop()
        mRootView = inflater.inflate(bindLayout(), container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        mIsPrepare = true
        initValue()
        initListener()
        initThis()
    }

    private fun initThis() {
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            // KEYCODE_BACK 拦截返回true
            keyCode == KeyEvent.KEYCODE_BACK && !mIsBackCancelable
        }
    }

    override fun onStart() {
        super.onStart()
        //设置 dialog 的宽高
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //设置 dialog 的背景为 null
        dialog?.window?.setBackgroundDrawable(null)
    }

    protected abstract fun bindLayout(): Int
    protected open fun initTop() {}
    protected open fun initView() {}
    protected open fun initValue() {}
    protected open fun initData() {}
    protected open fun initListener() {}

    fun showDialog(manager: FragmentManager?, tag: String = mTag, isResume: Boolean = true) {
        Logger.d("show#tag=${tag}")
        if (manager == null) return
        if (!isShowing()) {
            val isAdded = isAdded() && manager.findFragmentByTag(tag) == null
            if (isResume) {
                if (!isAdded) {
                    show(manager, tag)
                } else {
                    val ft = manager.beginTransaction()
                    ft.show(this)
                    ft.commit()
                }
            } else {
                val ft = manager.beginTransaction()
                if (!isAdded) {
                    ft.add(this, tag)
                } else {
                    ft.show(this)
                }
                ft.commitAllowingStateLoss()
            }
        }

//        val ft = manager.beginTransaction()
//        val fm = manager.findFragmentByTag(tag)
////        if (!this.isAdded) {
//        ft.add(this, tag)
//        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
//        ft.commitAllowingStateLoss()
////        } else {
////            ft.show(this)
////            ft.commitAllowingStateLoss()
////        }
    }

    fun isShowing(): Boolean {
        return dialog != null && dialog!!.isShowing
    }

    /**
     * 关闭DialogFragment
     * @param isResume 在Fragment中使用可直接传入isResumed()
     * 在FragmentActivity中使用可自定义全局变量 boolean isResumed 在onResume()和onPause()中分别传人判断为true和false
     */
    fun dismiss(isResume: Boolean) {
        if (isResume) {
            dismiss()
        } else {
            dismissAllowingStateLoss()
        }
    }

    override fun dismiss() {
        if (isShowing()) {
            super.dismiss()
        }
    }

    override fun dismissAllowingStateLoss() {
        if (isShowing()) {
            super.dismissAllowingStateLoss()
        }
    }

    //设置外面透明
    fun setOutBackgroundTransparent() {
        dialog?.window?.let {
            val windowParams = it.attributes
            windowParams.dimAmount = 0.0f
            it.attributes = windowParams
        }
    }

    //设置返回键不取消
    fun setIsBackCanceled(isBackCancelable: Boolean) {
        mIsBackCancelable = isBackCancelable
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}