package com.app.common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.support.v4.widget.DrawerLayout
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout

/**
 * @author zsl
 * @date 2018/6/13
 * @description StatusBar 和 NavigationBar 的工具类
 */
object AndroidBarUtils {

    private val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
    private val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
    private val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"

    /**
     * 设置透明StatusBar,默认文字为白色
     *
     * @param activity Activity
     */
    fun setTranslucent(activity: Activity) {
        setTranslucentStatusBar(activity)
    }

    /**
     * 设置 DrawerLayout 在4.4版本下透明，不然会出现白边
     *
     * @param drawerLayout DrawerLayout
     */
    fun setTranslucentDrawerLayout(drawerLayout: DrawerLayout?) {
        if (drawerLayout != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawerLayout.fitsSystemWindows = true
            drawerLayout.clipToPadding = false
        }
    }

    /**
     * 设置透明StatusBar
     *
     * @param activity Activity
     */
    private fun setTranslucentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        val window = activity.window
        //透明状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上版本
            createNavBar(activity)
        } else {
            //4.4版本
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * Android 6.0使用原始的主题适配
     *
     * @param activity Activity
     * @param darkMode 是否是黑色模式
     */
    fun setBarDarkMode(activity: Activity, darkMode: Boolean) {
        val window = activity.window ?: return
//设置StatusBar模式
        if (darkMode) {//黑色模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//设置statusBar和navigationBar为黑色
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {//设置statusBar为黑色
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        } else {//白色模式
            var statusBarFlag = View.SYSTEM_UI_FLAG_VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarFlag = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//设置statusBar为白色，navigationBar为灰色
                //                int navBarFlag = window.getDecorView().getSystemUiVisibility()
                //                        & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;//如果想让navigationBar为白色，那么就使用这个代码。
                val navBarFlag = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.decorView.systemUiVisibility = navBarFlag or statusBarFlag
            } else {
                window.decorView.systemUiVisibility = statusBarFlag
            }
        }
        setHuaWeiStatusBar(darkMode, window)
    }

    /**
     * 设置华为手机 StatusBar
     *
     * @param darkMode 是否是黑色模式
     * @param window   window
     */
    private fun setHuaWeiStatusBar(darkMode: Boolean, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = darkMode
                field.setInt(window.decorView, Color.TRANSPARENT)  //改为透明
            } catch (e: ClassNotFoundException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            } catch (e: IllegalAccessException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            } catch (e: NoSuchFieldException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            }

        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private fun getStatusBarHeight(context: Activity): Int {
        // 获得状态栏高度
        return getBarHeight(context, STATUS_BAR_HEIGHT_RES_NAME)
    }

    /**
     * 获取导航栏高度
     *
     * @param activity activity
     * @return 导航栏高度
     */
    private fun getNavigationBarHeight(activity: Activity): Int {
        return if (hasNavBar(activity)) {
            // 获得导航栏高度
            getBarHeight(activity, NAV_BAR_HEIGHT_RES_NAME)
        } else {
            0
        }
    }

    /**
     * 获取横屏状态下导航栏的宽度
     *
     * @param activity activity
     * @return 导航栏的宽度
     */
    private fun getNavigationBarWidth(activity: Activity): Int {
        return if (hasNavBar(activity)) {
            // 获得导航栏高度
            getBarHeight(activity, NAV_BAR_WIDTH_RES_NAME)
        } else {
            0
        }
    }

    /**
     * 获取Bar高度
     *
     * @param context context
     * @param barName 名称
     * @return Bar高度
     */
    private fun getBarHeight(context: Context, barName: String): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier(barName, "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 是否有NavigationBar
     *
     * @param activity 上下文
     * @return 是否有NavigationBar
     */
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

    /**
     * 设置BarPaddingTop
     *
     * @param context Activity
     * @param view    View[ToolBar、TitleBar、navigationView.getHeaderView(0)]
     */
    fun setBarPaddingTop(context: Activity, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val paddingStart = view.paddingStart
            val paddingEnd = view.paddingEnd
            val paddingBottom = view.paddingBottom
            val statusBarHeight = getStatusBarHeight(context)
            //改变titleBar的高度
            val lp = view.layoutParams
            lp.height += statusBarHeight
            view.layoutParams = lp
            //设置paddingTop
            view.setPaddingRelative(paddingStart, statusBarHeight, paddingEnd, paddingBottom)
        }
    }

    /**
     * 创建Navigation Bar
     *
     * @param activity 上下文
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNavBar(activity: Activity) {
        val navBarHeight = getNavigationBarHeight(activity)
        val navBarWidth = getNavigationBarWidth(activity)
        if (navBarHeight > 0 && navBarWidth > 0) {
            //创建NavigationBar
            val navBar = View(activity)
            val pl: FrameLayout.LayoutParams
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                pl = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navBarHeight)
                pl.gravity = Gravity.BOTTOM
            } else {
                pl = FrameLayout.LayoutParams(navBarWidth, ViewGroup.LayoutParams.MATCH_PARENT)
                pl.gravity = Gravity.END
            }
            navBar.layoutParams = pl
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                navBar.setBackgroundColor(Color.parseColor("#fffafafa"))
            } else {
                navBar.setBackgroundColor(Color.parseColor("#40000000"))
            }
            //添加到布局当中
            val decorView = activity.window.decorView as ViewGroup
            decorView.addView(navBar)
            //设置主布局PaddingBottom
            val contentView = decorView.findViewById<ViewGroup>(android.R.id.content)
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                contentView.setPaddingRelative(0, 0, 0, navBarHeight)
            } else {
                contentView.setPaddingRelative(0, 0, navBarWidth, 0)
            }

        }
    }
}
