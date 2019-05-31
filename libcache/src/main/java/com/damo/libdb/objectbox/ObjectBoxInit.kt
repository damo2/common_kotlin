package com.damo.libdb.objectbox

import android.content.Context
import android.util.Log
import com.damo.libdb.BuildConfig
import com.damo.libdb.objectbox.bean.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * Created by wangru
 * Date: 2018/12/27  14:13
 * mail: 1902065822@qq.com
 * describe:
 */
object ObjectBoxInit {
    lateinit var boxStore: BoxStore
        private set

    fun build(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(context)
            Log.i("ObjectBoxInit", "Started: $started")
        }

        // Example how you could use a custom dir in "external storage"
        // (Android 6+ note: give the app storage permission in app info settings)
//        val directory = File(Environment.getExternalStorageDirectory(), "objectbox-notes");
//        boxStore = MyObjectBox.builder().androidContext(context.applicationContext)
//                .directory(directory)
//                .build()
    }

}