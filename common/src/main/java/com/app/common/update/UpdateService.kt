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
import com.app.common.logger.logd
import com.app.common.utils.ActivityStack

/**
 * 更新app
 * Created by wr
 * Date: 2018/10/31  20:10
 * describe:
 * 启动的Activity 需要使用了ActivityStack管理，ActivityStack.top()取到Activity去安装apk
 */

class UpdateService : Service() {
    private val mNotificationUtils by lazy { NotificationUtils(applicationContext) }
    private val notificationManager by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
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
            val apkDownUrl = it.getStringExtra(ConstUpdate.KEY_DOWN_APK_URL)
            val apkInstallPath = it.getStringExtra(ConstUpdate.KEY_INSTALL_APK_PATH)
            downApk(apkDownUrl, apkInstallPath)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun downApk(downUrl: String, installPath: String) {
        var progress = -1
        RequestFileManager.downloadFile(downUrl, installPath, { file ->
            mNotificationUtils.cancelDownLoad()
            //下载完成，安装apk
            ActivityStack.top()?.let {
                InstallApp(file).installProcess(it)
            }
            stopSelf()
        }, { e ->
            stopSelf()
            logd(e.message ?: "下载apk异常")
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