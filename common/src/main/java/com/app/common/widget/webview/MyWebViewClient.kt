package com.app.common.widget.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.*

class MyWebViewClient : WebViewClient() {
    private val mListeners = ArrayList<InvokeListener>()

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.startsWith("doraemon://invokeNative")) {
            handleInvokeFromJs(url)
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    private fun handleInvokeFromJs(url: String) {
        for (listener in mListeners) {
            listener.onNativeInvoke(url)
        }
    }

    fun addInvokeListener(listener: InvokeListener) {
        mListeners.add(listener)
    }

    fun removeInvokeListener(listener: InvokeListener) {
        mListeners.remove(listener)
    }

    interface InvokeListener {
        fun onNativeInvoke(url: String)
    }
}