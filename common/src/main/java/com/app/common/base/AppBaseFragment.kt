package com.app.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.common.base.function.Functions
import com.app.common.logger.Logger
import com.app.common.view.LoadingDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AppBaseFragment : Fragment(), IBase {
    protected lateinit var mRootView: View
    //是否对用户可见
    protected var mIsVisible: Boolean = false
    // 是否加载完成 当执行完onCreateView,View的初始化方法后方法后即为true
    protected var mIsPrepare: Boolean = false
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mLoadingDialog by lazy { LoadingDialogFragment() }

    protected var mActivity: Activity? = null
    private var mBaseActivity: AppBaseActivity? = null
    //函数的集合
    protected var mFunctions: Functions? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
        if (context is AppBaseActivity) {
            mBaseActivity = context
            mBaseActivity?.setFunctionsForFragment(javaClass.simpleName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mContext = App.instance
        mRootView = inflater.inflate(bindLayout(), container, false)
//        if (isInject) ARouter.getInstance().inject(this)
        initTop()
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        mIsPrepare = true
        initListener()
        initValue()
        onVisibleToUser()
    }

    protected abstract fun bindLayout(): Int
    override fun initTop() {}
    override fun initData() {}
    override fun initView() {}
    override fun initValue() {}
    override fun initListener() {}

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        this.mIsVisible = !hidden
        if (!activity.isFinishing && !activity.isDestroyed) {
            if (mIsVisible) onVisibleToUser() else onInVisibleToUser()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.mIsVisible = isVisibleToUser
        if (mIsVisible) onVisibleToUser() else onInVisibleToUser()
    }

    private fun prepareFetchData() {
        if (mIsVisible && mIsPrepare) {
            onLazyLoad()
        }
    }

    //用户可见时执行的操作
    private fun onVisibleToUser() = if (mIsPrepare) onLazyLoad() else {
    }

    //用户不可见时执行的操作
    private fun onInVisibleToUser() = if (mIsPrepare) onLazyClear() else {
    }

    // 懒加载，仅当用户可见切view初始化结束后才会执行
    protected open fun onLazyLoad() {
        Logger.i("${this.javaClass.simpleName}#onLazyLoad")
    }

    //不可见时清理
    protected open fun onLazyClear() {
        Logger.i("${this.javaClass.simpleName}#onLazyClear")
    }

    /**
     * @param isCanCancel 是否能被取消
     */
    fun showLoadingDialog(isCanCancel: Boolean = true) {
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.isCancelable = isCanCancel
            mLoadingDialog.show(activity.supportFragmentManager, "loading", isResumed)
        }
    }

    fun dismissLoadingDialog() {
        mLoadingDialog.dismiss()
    }

    fun addSubscription(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    //activity调用此方法进行设置Functions
    fun setFunctions(functions: Functions) {
        this.mFunctions = functions
    }

    override fun onDestroy() {
        mIsPrepare = false
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}