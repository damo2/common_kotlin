package com.damo.test.activity.test;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import com.damo.test.R;

import androidx.core.app.NotificationCompat;

/**
 * Created by wr
 * Date: 2019/5/31  15:56
 * mail: 1902065822@qq.com
 * describe:
 */
public class Test {
    private static final String PRIMARY_CHANNEL = "message";

    public static void showInNotificationBar(Context ctx, String title, String ticker, Bitmap iconBitmap, int notificationId, Intent intent) {
        NotificationManager notifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyMgr == null) {
            return;
        }
        NotificationCompat.Builder builder;
        //判断是否是8.0Android.O
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
            "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notifyMgr.createNotificationChannel(chan1);
            builder = new NotificationCompat.Builder(ctx, PRIMARY_CHANNEL);
        } else {
            builder = new NotificationCompat.Builder(ctx);
        }
        builder.setContentTitle(title);
        builder.setContentText(ticker);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);

        //        if (configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.VIBRATION)) {
        //            // delay 0ms, vibrate 200ms, delay 250ms, vibrate 200ms
        //            long[] vibrate = {0, 200, 250, 200};
        //            builder.setVibrate(vibrate);
        //        } else {
        //            logger.d("notification#setting is not using vibration");
        //        }
        //
        //        // sound
        //        if (configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.SOUND)) {
        //            builder.setDefaults(Notification.DEFAULT_SOUND);
        //        } else {
        //            logger.d("notification#setting is not using sound");
        //        }
        if (iconBitmap != null) {
            builder.setLargeIcon(iconBitmap);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notifyMgr.notify(notificationId, notification);
    }
}
