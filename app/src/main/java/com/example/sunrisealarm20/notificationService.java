package com.example.sunrisealarm20;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Objects;

public class notificationService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cmd = intent.getStringExtra("cmd");
        if(Objects.equals(cmd, "Start")) {
            double unix = intent.getDoubleExtra("Unix", 0);
            String alarmTime = intent.getStringExtra("AlarmTime");
            Log.d("DEFAULT", "Service has been started!");

            //set intent for alarm screen from notification
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);

            String CHANNEL_ID = "My Channel";
            //build notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.alarm_icon)
                    .setContentTitle("Alarm Scheduled")
                    .setContentText("Alarm Scheduled for " + alarmTime)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notif = builder.build();
            if (ActivityCompat.checkSelfPermission(notificationService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ERROR", "Permission not granted");
            }
            startForeground(1, notif);
        }
        else {
            Log.d("DEFAULT", "Service has been Stopped!");
            stopSelf();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
