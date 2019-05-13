package com.app.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build

/**
 * Created by wr
 * Date: 2019/5/10  16:46
 * mail: 1902065822@qq.com
 * describe:
 */
class MyContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context): ContextWrapper {
            var context = context
            val resources = context.resources
            val newConfig = Configuration()
            val metrics = resources.displayMetrics
            newConfig.setToDefaults()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //如果没有设置densityDpi, createConfigurationContext对字体大小设置限制无效
                newConfig.densityDpi = metrics.densityDpi
                context = context.createConfigurationContext(newConfig)
            } else {
                resources.updateConfiguration(newConfig, resources.displayMetrics)
            }
            return MyContextWrapper(context)
        }
    }
}