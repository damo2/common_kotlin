package com.app.common.update

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.common.R


/**
 * Created by wangru
 * Date: 2018/7/9  13:26
 * mail: 1902065822@qq.com
 * describe:
 */
class NotificationUtils(val context: Context) {
    private val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification26.initChannel(context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        }
    }


    private val buildDown26: Notification.Builder by lazy {
        Notification26.initDownLoad(context)
    }
    private val buildDown25: NotificationCompat.Builder by lazy {
        Notification25.initDownLoad(context)
    }

    /**
     *初始化或更新下载进度
     */
    fun updateDownLoad(progress: Int = 0) {
        val notificationDownLoad: Notification =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    buildDown26.setContentTitle("下载进度:$progress%").setProgress(100, progress, false).build()
                else
                    buildDown25.setContentTitle("下载进度:$progress%").setProgress(100, progress, false).build()

        notificationManager.notify(NotificationId.DOWNLOAD, notificationDownLoad)
    }

    fun cancelDownLoad() {
        notificationManager.cancel(NotificationId.DOWNLOAD)
    }

    object Notification25 {
        fun initDownLoad(context: Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_app_update)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setVibrate(longArrayOf(0, 0, 0, 0))
                    .addAction(R.drawable.common_toast_error, "取消", null)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    object Notification26 {

        fun initChannel(notificationManager: NotificationManager) {
            createNotificationChannel(notificationManager, ChannelId.DOWNLOAD, ChannelName.DOWNLOAD, NotificationManager.IMPORTANCE_DEFAULT, isVibrate = false, isSound = false)
        }

        private fun createNotificationChannel(notificationManager: NotificationManager, channelId: String, channelName: String, importance: Int, isVibrate: Boolean = true, isSound: Boolean = true) {
            if (notificationManager.getNotificationChannel(channelId) != null) return
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                enableLights(true)
                //小红点颜色
                setLightColor(Color.GREEN)
                //是否在久按桌面图标时显示此渠道的通知
                setShowBadge(true)
                if (!isSound) setSound(null, null)
                enableVibration(isVibrate)
                if (!isVibrate) vibrationPattern = longArrayOf(0L)

                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }

        fun initDownLoad(context: Context): Notification.Builder {
            return Notification.Builder(context, ChannelId.DOWNLOAD)
                    .setSmallIcon(R.drawable.ic_app_update)
                    .addAction(R.drawable.common_toast_error, "取消", null)
        }
    }

}