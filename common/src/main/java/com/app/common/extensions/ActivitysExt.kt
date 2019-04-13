package com.app.common.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


private val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
private val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
private val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"
//屏幕宽
fun Activity.getScreenWidth(): Int {
    val localDisplayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
    return localDisplayMetrics.widthPixels
}

//屏幕高
fun Activity.getScreenHeight(): Int {
    val localDisplayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
    return localDisplayMetrics.heightPixels
}

/**
 * 状态栏高度
 */
fun Activity.getStatusBarHeight(): Int = getBarHeight(this, STATUS_BAR_HEIGHT_RES_NAME)

/**
 * 获取导航栏高度
 */
fun Activity.getNavigationBarHeight(): Int = getBarHeight(this, NAV_BAR_HEIGHT_RES_NAME)

/**
 * 获取横屏状态下导航栏的宽度
 */
fun Activity.getNavigationBarWidth(): Int = getBarHeight(this, NAV_BAR_WIDTH_RES_NAME)


//屏幕密度
fun Activity.getDensity(): Int {
    val dm = resources.displayMetrics
    return dm.density.toInt()
}

fun Activity.getDensityDpi(): Int {
    val dm = resources.displayMetrics
    return dm.densityDpi
}

private fun getBarHeight(context: Context, barName: String): Int {
    // 获得状态栏高度
    val resourceId = context.resources.getIdentifier(barName, "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}

private fun hasNavBar(activity: Activity): Boolean {
    val windowManager = activity.windowManager
    val d = windowManager.defaultDisplay

    val realDisplayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        d.getRealMetrics(realDisplayMetrics)
    }

    val realHeight = realDisplayMetrics.heightPixels
    val realWidth = realDisplayMetrics.widthPixels

    val displayMetrics = DisplayMetrics()
    d.getMetrics(displayMetrics)

    val displayHeight = displayMetrics.heightPixels
    val displayWidth = displayMetrics.widthPixels

    return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
}

 //隐藏状态栏
fun Activity.statusHind() {
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

//沉浸式状态栏
fun Activity.statusTranslucent() {
    //透明状态栏
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

//改变背景亮度
fun Activity.backgroundAlphaExt(bgAlpha: Float) {
    val lp = this.window?.attributes
    //0.0-1.0
    lp?.alpha = bgAlpha
//    if (bgAlpha == 1f) {
//        //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//        this.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//    } else {
//        //此行代码主要是解决在华为手机上半透明效果无效的bug
//        this.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//    }
    this.window?.attributes = lp
}

//view是否在屏幕中可见
fun Activity.viewIsVisible(view: View): Boolean {
    val p = Point()
    windowManager.defaultDisplay.getSize(p)
    val screenWidth = p.x
    val screenHeight = p.y
    //屏幕范围
    val rect = Rect(0, 0, screenWidth, screenHeight)
    //view是否在屏幕中
    return view.getLocalVisibleRect(rect)
}


fun Activity.hindSoftInputExt() {
    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
        if (getCurrentFocus() != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}