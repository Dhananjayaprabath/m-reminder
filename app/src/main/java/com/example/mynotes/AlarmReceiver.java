package com.example.mynotes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity. class ) ;
        intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK | Intent. FLAG_ACTIVITY_CLEAR_TASK ) ;
        PendingIntent pendingIntent = PendingIntent. getActivity ( context, 0 , i , 0 ) ;



        String titi = intent.getStringExtra("notitext");




        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle( intent.getStringExtra("notitext"))
                .setContentText("You have Meeting Now")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int id =1542;

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id,builder.build());





    }
}
