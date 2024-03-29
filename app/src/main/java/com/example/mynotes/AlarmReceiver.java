package com.example.mynotes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String titi = intent.getStringExtra("notitext");
        String day =intent.getStringExtra("notidate");
        String notiids=intent.getStringExtra("notiid");
        int notifiid =Integer.parseInt(notiids);

        Intent i = new Intent(context, MainActivity. class ) ;
        intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK | Intent. FLAG_ACTIVITY_CLEAR_TASK ) ;
        PendingIntent pendingIntent = PendingIntent. getActivity ( context, notifiid , i , 0 ) ;






        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"Meeting Reminder")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(titi)
                .setContentText("You have meeting at "+day)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);




        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notifiid,builder.build());





    }
}
