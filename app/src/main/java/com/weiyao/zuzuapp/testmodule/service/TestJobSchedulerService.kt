package com.weiyao.zuzuapp.testmodule.service


import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Build

import com.app.common.logger.Logger
import com.weiyao.zuzuapp.R

import java.util.Random

import androidx.core.app.NotificationCompat

class TestJobSchedulerService : JobService() {

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        Logger.d("定时执行任务完成")
        sendNotification()
        jobFinished(jobParameters, false)
        return true
    }

    private fun sendNotification() {
        val id = Random(System.nanoTime()).nextInt()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道通知
            notificationManager.createNotificationChannel(createNotificationChannel(NOTIFICATION_CHANNEL_ID, "kad"))
            builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(this, "")
        }
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        builder.setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("定时通知")
                .setContentText("我是定时通知，定时任务已经执行")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
        val notification = builder.build()
        notificationManager.notify(id, notification)

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): NotificationChannel {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.lightColor = Color.RED//设置小红点颜色
        channel.enableLights(true)//是否在桌面icon右上角展示小红点
        channel.setShowBadge(true)//长按桌面应用图标是否会显示渠道通知
        return channel
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    companion object {
        private val TIME_INTERVAL = (10 * 1000).toLong()//每隔TIME_INTERVAL毫秒运行一次
        private val TAG = TestJobSchedulerService::class.java.simpleName
        private val NOTIFY_ID = 100
        val NOTIFICATION_CHANNEL_ID = "kad_channel_id"

        fun start(context: Context) {
            val mJobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val builder = JobInfo.Builder(100, ComponentName(context, TestJobSchedulerService::class.java))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMinimumLatency((10 * 1000).toLong())
            } else {
                builder.setPeriodic((10 * 1000).toLong())
            }
        }

        fun startJobScheduler(context: Context) {
            val mJobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val id = 110
            val time = 16 * 60 * 1000
            Logger.d("开启AstJobService id=$id")
            mJobScheduler.cancel(id)
            val builder = JobInfo.Builder(id, ComponentName(context, TestJobSchedulerService::class.java))
            if (Build.VERSION.SDK_INT >= 24) {
                builder.setMinimumLatency(time.toLong()) //执行的最小延迟时间
                builder.setOverrideDeadline(time.toLong())  //执行的最长延时时间
                builder.setBackoffCriteria(time.toLong(), JobInfo.BACKOFF_POLICY_LINEAR)//线性重试方案
            } else {
                builder.setPeriodic(time.toLong())
            }
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            builder.setRequiresCharging(true) // 当插入充电器，执行该任务
            val info = builder.build()
            mJobScheduler.schedule(info) //开始定时执行该系统任务
        }
    }
}