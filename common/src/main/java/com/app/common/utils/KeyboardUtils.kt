package com.app.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.app.common.extensions.dp2px
import com.app.common.extensions.getActivityRootExt


/**
 * 软键盘工具类
 *
 * https://github.com/yshrsmz/KeyboardVisibilityEvent/blob/master/keyboardvisibilityevent/src/main/java/net/yslibrary/android/keyboardvisibilityevent/KeyboardVisibilityEvent.java
 */

object KeyboardUtils {
    /**
     * 显示软键盘的延迟时间
     */
    val SHOW_KEYBOARD_DELAY_TIME = 200
    private val TAG = "QMUIKeyboardHelper"
    val KEYBOARD_VISIBLE_THRESHOLD_DP = 100


    fun showKeyboard(editText: EditText, delay: Boolean) {
        showKeyboard(editText, if (delay) SHOW_KEYBOARD_DELAY_TIME else 0)
    }


    /**
     * 针对给定的editText显示软键盘（editText会先获得焦点）. 可以和[.hideKeyboard]
     * 搭配使用，进行键盘的显示隐藏控制。
     */

    fun showKeyboard(editText: EditText?, delay: Int) {
        if (null == editText)
            return

        if (!editText.requestFocus()) {
            Log.w(TAG, "showSoftInput() can not get focus")
            return
        }
        if (delay > 0) {
            editText.postDelayed({
                val imm = editText.context.applicationContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }, delay.toLong())
        } else {
            val imm = editText.context.applicationContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * 隐藏软键盘 可以和[.showKeyboard]搭配使用，进行键盘的显示隐藏控制。
     *
     * @param view 当前页面上任意一个可用的view
     */
    fun hideKeyboard(view: View?): Boolean {
        if (null == view)
            return false

        val inputManager = view.context.applicationContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 即使当前焦点不在editText，也是可以隐藏的。
        return inputManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 软键盘显示状态改变监听
     *
     * @param activity Activity
     */
    fun Activity.setKeyboardVisibleListenerExt(listener: ((isOpen: Boolean, heightDiff: Int) -> Boolean)?) {

        val activityRoot = getActivityRootExt()

        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

            private val r = Rect()

            private val visibleThreshold = Math.round(dp2px(KEYBOARD_VISIBLE_THRESHOLD_DP).toFloat())

            private var wasOpened = false

            override fun onGlobalLayout() {
                activityRoot.getWindowVisibleDisplayFrame(r)

                val heightDiff = activityRoot.rootView.height - r.height()

                val isOpen = heightDiff > visibleThreshold

                if (isOpen == wasOpened) {
                    // keyboard state has not changed
                    return
                }

                wasOpened = isOpen

                val removeListener = listener?.invoke(isOpen, heightDiff)
                if (removeListener == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        activityRoot.viewTreeObserver
                                .removeOnGlobalLayoutListener(this)
                    } else {
                        activityRoot.viewTreeObserver
                                .removeGlobalOnLayoutListener(this)
                    }
                }
            }
        }
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        application
                .registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks(this) {
                    override fun onTargetActivityDestroyed() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            activityRoot.viewTreeObserver
                                    .removeOnGlobalLayoutListener(layoutListener)
                        } else {
                            activityRoot.viewTreeObserver
                                    .removeGlobalOnLayoutListener(layoutListener)
                        }
                    }
                })
    }

    /**
     * Determine if keyboard is visible
     *
     * @param activity Activity
     * @return Whether keyboard is visible or not
     */
    fun Activity.isKeyboardVisibleExt(): Boolean {
        val r = Rect()
        val activityRoot = getActivityRootExt()
        val visibleThreshold = Math.round(dp2px(KEYBOARD_VISIBLE_THRESHOLD_DP).toFloat())

        activityRoot.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold
    }
}