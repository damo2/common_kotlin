package com.app.common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.NonNull
import com.app.common.extensions.createNavBarExt
import com.app.common.extensions.navigationBarHeightExt
import com.app.common.extensions.navigationBarWidthExt
import com.app.common.extensions.statusBarHeightExt


/**
 * Created by wr
 * Date: 2019/5/10  14:13
 * mail: 1902065822@qq.com
 * describe:
 */
object ActivityUtils {
    private val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
    private val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
    private val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"
    //屏幕宽
    fun getScreenWidth(activity: Activity): Int {
        val localDisplayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
        return localDisplayMetrics.widthPixels
    }

    //屏幕高
    fun getScreenHeight(activity: Activity): Int {
        val localDisplayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
        return localDisplayMetrics.heightPixels
    }

    /**
     * 状态栏高度
     */
    fun getStatusBarHeight(activity: Activity): Int = getBarHeight(activity, STATUS_BAR_HEIGHT_RES_NAME)

    /**
     * 获取导航栏高度
     */
    fun getNavigationBarHeight(activity: Activity): Int = getBarHeight(activity, NAV_BAR_HEIGHT_RES_NAME)

    /**
     * 获取横屏状态下导航栏的宽度
     */
    fun getNavigationBarWidth(activity: Activity): Int = getBarHeight(activity, NAV_BAR_WIDTH_RES_NAME)

    fun getBarHeight(context: Context, barName: String): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier(barName, "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }


    //屏幕密度
    fun getDensity(activity: Activity): Int {
        val dm = activity.resources.displayMetrics
        return dm.density.toInt()
    }

    fun getDensityDpi(activity: Activity): Int {
        val dm = activity.resources.displayMetrics
        return dm.densityDpi
    }


    /**
     * 获取activity的根view
     */
    fun getActivityRoot(activity: Activity): View {
        return (activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)).getChildAt(0)
    }

    fun hasNavBar(activity: Activity): Boolean {
        val d = activity.windowManager.defaultDisplay

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
    fun statusHind(activity: Activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    //沉浸式状态栏
    fun statusTranslucent(activity: Activity) {
        //透明状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        //透明状态栏
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上版本
            activity.createNavBarExt()
        } else {
            //4.4版本
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    //改变背景亮度
    fun backgroundAlpha(activity: Activity, bgAlpha: Float) {
        val lp = activity.window?.attributes
        //0.0-1.0
        lp?.alpha = bgAlpha
//    if (bgAlpha == 1f) {
//        //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//        this.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//    } else {
//        //此行代码主要是解决在华为手机上半透明效果无效的bug
//        this.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//    }
        activity.window?.attributes = lp
    }

    //view是否在屏幕中可见
    fun viewIsVisible(activity: Activity, view: View): Boolean {
        val p = Point()
        activity.windowManager.defaultDisplay.getSize(p)
        val screenWidth = p.x
        val screenHeight = p.y
        //屏幕范围
        val rect = Rect(0, 0, screenWidth, screenHeight)
        //view是否在屏幕中
        return view.getLocalVisibleRect(rect)
    }

    /**
     * 创建Navigation Bar
     *
     * @param activity 上下文
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun createNavBar(activity: Activity) {
        val navBarHeight = activity.navigationBarHeightExt
        val navBarWidth = activity.navigationBarWidthExt
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

    /**
     * 设置BarPaddingTop
     *
     * @param context Activity
     * @param view    View[ToolBar、TitleBar、navigationView.getHeaderView(0)]
     */
    fun setBarPaddingTop(activity: Activity, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val paddingStart = view.paddingStart
            val paddingEnd = view.paddingEnd
            val paddingBottom = view.paddingBottom
            val statusBarHeight = activity.statusBarHeightExt
            //改变titleBar的高度
            val lp = view.layoutParams
            lp.height += statusBarHeight
            view.layoutParams = lp
            //设置paddingTop
            view.setPaddingRelative(paddingStart, statusBarHeight, paddingEnd, paddingBottom)
        }
    }

    /**
     * Return the activity by view.
     *
     * @param view The view.
     * @return the activity by view.
     */
    fun getActivityByView(@NonNull view: View): Activity? {
        return getActivityByContext(view.context)
    }

    /**
     * Return the activity by context.
     *
     * @param context The context.
     * @return the activity by context.
     */
    fun getActivityByContext(@NonNull context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}