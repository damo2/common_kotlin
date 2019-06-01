package com.app.common.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.app.common.extensions.isConnectedExt
import android.widget.LinearLayout
import android.view.ViewGroup





/**
 * Created by wr
 * Date: 2019/5/21  13:22
 * mail: 1902065822@qq.com
 * describe:
 * https://www.cnblogs.com/dongweiq/p/7458207.html
 *
 * @sample
 *   //直接 new WebView 并传入 application context 代替在 XML 里面声明以防止 activity 引用被滥用，能解决90+%的 WebView 内存泄漏。
 *   val webView = WebView(context)
 *   linearLayout.addView(webView)
 */
object WebViewUtil {
    fun initWeb(context: Context, webView: WebView) {
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)//硬件解码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//软件解码
        }
        webView.isSaveEnabled = true
        webView.keepScreenOn = true

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (!webView.settings.loadsImagesAutomatically) {
                    webView.settings.loadsImagesAutomatically = true
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url)
                    return false
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
                return true
            }
        }
        //支持下载
        webView.setDownloadListener { paramAnonymousString1, paramAnonymousString2, paramAnonymousString3, paramAnonymousString4, paramAnonymousLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramAnonymousString1)
            context.startActivity(intent)
        }
    }

    fun initWebSettings(context: Context, webSettings: WebSettings): WebSettings {
        // 是否支持Javascript，默认值false
        webSettings.javaScriptEnabled = true

        // 是否支持viewport属性，默认值 false
        webSettings.useWideViewPort = true// 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        webSettings.loadWithOverviewMode = true// 是否使用overview mode加载页面，默认值 false ,当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL// 布局算法，自适应屏幕

        // 资源加载
        webSettings.loadsImagesAutomatically = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT // 是否自动加载图片
        webSettings.blockNetworkImage = false // 禁止加载网络图片
        webSettings.blockNetworkLoads = false // 禁止加载所有网络资源

        // 缩放(zoom)
        webSettings.setSupportZoom(true) // 是否支持缩放
        webSettings.builtInZoomControls = true // 是否使用内置缩放机制,由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.displayZoomControls = false // 是否显示缩放控件


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 用户是否需要通过手势播放媒体(不会自动播放)，默认值 true
            webSettings.mediaPlaybackRequiresUserGesture = true
        }

        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 缓存(cache) 此API已废弃，参考：https://developer.mozilla.org/zh-CN/docs/Web/HTML/Using_the_application_cache
        webSettings.setAppCacheEnabled(true) // 默认值 false
        webSettings.setAppCachePath(context.cacheDir.absolutePath)

        // 存储(storage)
        webSettings.domStorageEnabled = true// 默认值 false 是否开启本地DomStorage存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSettings.databaseEnabled = true // 默认值 false 此API已不推荐使用，参考：https://www.w3.org/TR/webdatabase/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //  WebSettings.LOAD_DEFAULT 如果本地缓存可用且没有过期则使用本地缓存，否加载网络数据 默认值
            //  WebSettings.LOAD_CACHE_ELSE_NETWORK 优先加载本地缓存数据，无论缓存是否过期
            //  WebSettings.LOAD_NO_CACHE  只加载网络数据，不加载本地缓存
            //  WebSettings.LOAD_CACHE_ONLY 只加载缓存数据，不加载网络数据
            webSettings.cacheMode = if (context.isConnectedExt()) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK//没网优先加载缓存数据
        }

        //弃用
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
//        webSettings.databasePath = context.getDir("database", Context.MODE_PRIVATE).path
//        webSettings.setGeolocationDatabasePath(context.filesDir.path)

        webSettings.pluginState = WebSettings.PluginState.ON//支持播放插件
        webSettings.savePassword = true//保存密码

        return webSettings
    }

    fun loadData(webView: WebView, content: String) {
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)//这种写法可以正确解码
    }

    //销毁 WebView
    fun removeWeb(webView: WebView) {
        webView.webViewClient = null
        webView.webChromeClient = null
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        (webView.parent as ViewGroup).removeView(webView)
        webView.destroy()
        //webView = null
    }
}
