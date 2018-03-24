package com.example.personal.project_android;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmBroadCustReciver extends BroadcastReceiver  {

    String s;
    int mNotificationId = 100;
    TextToSpeech t1;

    public void onReceive(Context context, Intent intent) {
        s = intent.getStringExtra("Notify");

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setAutoCancel(true)
                .setContentText(s)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}