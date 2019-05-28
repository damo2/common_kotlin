package com.damo.test.service;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.app.common.logger.Logger;
import com.damo.test.R;

import java.util.Random;

import androidx.core.app.NotificationCompat;

public class TestJobSchedulerService extends JobService {
    private static final long TIME_INTERVAL = 10 * 1000;//每隔TIME_INTERVAL毫秒运行一次
    private static String TAG = TestJobSchedulerService.class.getSimpleName();
    private static final int NOTIFY_ID = 100;
    public static final String NOTIFICATION_CHANNEL_ID = "kad_channel_id";

    public TestJobSchedulerService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Logger.INSTANCE.d("定时执行任务完成");
        sendNotification();
        jobFinished(jobParameters, false);
        return true;
    }

    private void sendNotification() {
        int id = new Random(System.nanoTime()).nextInt();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道通知
            notificationManager.createNotificationChannel(createNotificationChannel(NOTIFICATION_CHANNEL_ID, "kad"));
            builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this, "");
        }
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setDefaults(Notification.DEFAULT_ALL)
        .setContentTitle("定时通知")
        .setContentText("我是定时通知，定时任务已经执行")
        .setAutoCancel(true)
        .setWhen(System.currentTimeMillis())
        .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notificationManager.notify(id, notification);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel(String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLightColor(Color.RED);//设置小红点颜色
        channel.enableLights(true);//是否在桌面icon右上角展示小红点
        channel.setShowBadge(true);//长按桌面应用图标是否会显示渠道通知
        return channel;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public static void start(Context context) {
        JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(context, TestJobSchedulerService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(10 * 1000);
        } else {
            builder.setPeriodic(10 * 1000);
        }
    }

    public static void startJobScheduler(Context context) {
        JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int id = 110;
        int time = 16 * 60 * 1000;
        Logger.INSTANCE.d("开启AstJobService id=" + id);
        mJobScheduler.cancel(id);
        JobInfo.Builder builder = new JobInfo.Builder(id, new ComponentName(context, TestJobSchedulerService.class));
        if (Build.VERSION.SDK_INT >= 24) {
            builder.setMinimumLatency(time); //执行的最小延迟时间
            builder.setOverrideDeadline(time);  //执行的最长延时时间
            builder.setBackoffCriteria(time, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
        } else {
            builder.setPeriodic(time);
        }
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(true); // 当插入充电器，执行该任务
        JobInfo info = builder.build();
        mJobScheduler.schedule(info); //开始定时执行该系统任务
    }
}