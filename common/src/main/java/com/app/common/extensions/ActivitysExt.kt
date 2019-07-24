package com.app.common.extensions

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import com.app.common.utils.ActivityUtils


//屏幕宽
val Activity.screenWidthExt: Int
    get() = ActivityUtils.getScreenWidth(this)

//屏幕高
val Activity.screenHeightExt: Int
    get() = ActivityUtils.getScreenHeight(this)

//状态栏高度
val Activity.statusBarHeightExt: Int
    get() = ActivityUtils.getStatusBarHeight(this)

//获取导航栏高度
val Activity.navigationBarHeightExt: Int
    get() = ActivityUtils.getNavigationBarHeight(this)

//获取横屏状态下导航栏的宽度
val Activity.navigationBarWidthExt: Int
    get() = ActivityUtils.getNavigationBarWidth(this)

//屏幕密度
val Activity.densityExt: Int
    get() = ActivityUtils.getDensity(this)

val Activity.densityDpiExt: Int
    get() = ActivityUtils.getDensityDpi(this)

//获取activity的根view
val Activity.activityRootExt: View
    get() = ActivityUtils.getActivityRoot(this)

val Activity.hasNavBarExt: Boolean
    get() = ActivityUtils.hasNavBar(this)

//隐藏状态栏
fun Activity.statusHind() = ActivityUtils.statusHind(this)

//沉浸式状态栏
fun Activity.statusTranslucent() = ActivityUtils.statusTranslucent(this)

//改变背景亮度
fun Activity.backgroundAlphaExt(bgAlpha: Float) = ActivityUtils.backgroundAlpha(this, bgAlpha)

//view是否在屏幕中可见
fun Activity.viewIsVisibleExt(view: View) = ActivityUtils.viewIsVisible(this, view)

/**
 * 创建Navigation Bar
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.createNavBarExt() = ActivityUtils.createNavBar(this)

/**
 * 设置BarPaddingTop
 * @param view    View[ToolBar、TitleBar、navigationView.getHeaderView(0)]
 */
fun Activity.setBarPaddingTopExt(view: View) = ActivityUtils.setBarPaddingTop(this, view)