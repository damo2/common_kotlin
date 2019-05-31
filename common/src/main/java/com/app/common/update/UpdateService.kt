package com.app.common.update

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.app.common.api.RequestFileManager
import com.app.common.logger.Logger
import com.app.common.utils.ActivityStack
import com.app.common.utils.StorageUtils
import java.io.File

/**
 * 更新app
 * Created by wr
 * Date: 2018/10/31  20:10
 * describe:
 */

class UpdateService : Service() {
    private val mNotificationUtils by lazy { NotificationUtils(applicationContext) }
    private val notificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    val DIR_APK_STR = "apk"

    private fun getApkPath(url: String) = StorageUtils.getPublicStorageDir(DIR_APK_STR) + File.separator + url.substring(url.lastIndexOf("/"), url.length)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ChannelId.SERVICE_UPDATE, ChannelName.SERVICE_UPDATE, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(this, ChannelId.SERVICE_UPDATE).build()
            startForeground(NotificationId.SERVICE_UPDATE, notification)
            Logger.d("onCreate#startForeground")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val url = it.getStringExtra(ConstUpdate.KEY_DOWN_APK_URL)
            downApk(url)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun downApk(url: String) {
        var progress = -1
        val urlPath = getApkPath(url)
        RequestFileManager.downloadFile(url, urlPath, { file ->
            mNotificationUtils.cancelDownLoad()
            ActivityStack.top()?.let {
                InstallApp(file).installProcess(it)
            }
            stopSelf()
        }, { e ->
            stopSelf()
        }, { totalLength, contentLength, done ->
            val curProgress = (totalLength * 100 / contentLength).toInt()
            if (curProgress != progress) {
                progress = curProgress
                mNotificationUtils.updateDownLoad(progress)
            }
            if (done) {
                notificationManager.cancel(NotificationId.SERVICE_UPDATE)
            }
        })
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

}