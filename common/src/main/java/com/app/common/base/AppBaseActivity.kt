package com.app.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.app.common.api.util.LifeCycleEvent
import com.app.common.utils.FixMemLeak
import com.app.common.utils.MyContextWrapper
import com.app.common.view.LoadingDialogFragment
import com.evernote.android.state.StateSaver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject


abstract class AppBaseActivity : AppCompatActivity(), IBase {
    protected var activity = this
    private val mCompositeDisposable by lazy { CompositeDisposable() }
    private val mLoadingDialog by lazy { LoadingDialogFragment() }
    private var mIsShowLoading = false
    protected val lifecycleSubject = PublishSubject.create<LifeCycleEvent>()
    var isResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null)
        }
        super.onCreate(savedInstanceState ?: Bundle())
//        StateSaver.restoreInstanceState(this, savedInstanceState)
        lifecycleSubject.onNext(LifeCycleEvent.CREATE)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initTop()
        bindLayout()?.let {
            setContentView(it)
        }
        initData()
        initView()
        initValue()
        initListener()
    }

    abstract fun bindLayout(): Int?
    override fun initTop() {}
    override fun initView() {}
    override fun initValue() {}
    override fun initData() {}
    override fun initListener() {}

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(LifeCycleEvent.START)
    }

    override fun onResume() {
        isResume = true
        super.onResume()
        lifecycleSubject.onNext(LifeCycleEvent.RESUME)
    }

    fun addSubscription(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * @param isCanCancel 是否能被取消
     */
    fun showLoadingDialog(isCanCancel: Boolean = true) {
        if (!mLoadingDialog.isShowing()) {
            mIsShowLoading = true
            mLoadingDialog.setIsBackCanceled(isCanCancel)
            mLoadingDialog.showDialog(supportFragmentManager, "loading", isResume)
        }
    }

    /**
     * 为fragment设置functions，具体实现子类来做
     * @param fragmentId
     */
    open fun setFunctionsForFragment(fragmentTag: String) {}

    fun dismissLoadingDialog() {
        mLoadingDialog.dismiss()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        StateSaver.saveInstanceState(this, outState)
    }

    override fun onPause() {
        isResume = false
        lifecycleSubject.onNext(LifeCycleEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleSubject.onNext(LifeCycleEvent.STOP)
        super.onStop()
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        FixMemLeak.fixLeak(applicationContext)
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY)
        super.onDestroy()
    }

    //防止手机改字体布局乱掉
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase))
    }

}