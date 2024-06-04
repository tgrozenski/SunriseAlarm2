package com.example.sunrisealarm20;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class notificationService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cmd = intent.getStringExtra("cmd");
        if(Objects.equals(cmd, "Start")) {
            Long unix = intent.getLongExtra("Unix", 0);
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
                    .setContentTitle("Alarm Scheduled for " + alarmTime)
                    .setContentText("Do not close this notification")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notif = builder.build();
            if (ActivityCompat.checkSelfPermission(notificationService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ERROR", "Permission not granted");
            }
            startForeground(1, notif);
            this.scheduleAlarm(unix);
            Log.d("DEFAULT", "Foreground Service is engaged");

        }
        else {
            Log.d("DEFAULT", "Service has been Stopped!");
            stopSelf();
        }
        return START_STICKY;
    }

    private void scheduleAlarm(long unix) {

        Intent service = new Intent(getApplicationContext(), ringEvent.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getApplicationContext(), 0, service, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //cancel any previous alarm set
        alarmmanager.cancel(pendingintent);

            Log.d("DEFAULT", "time to mili:" + unix + " Current Time" + System.currentTimeMillis());
                if (alarmmanager.canScheduleExactAlarms()) {
                    Log.d("DEFAULT", "ALARM Should b3 scheduled! " + unix + " Current Time" + System.currentTimeMillis());
                    alarmmanager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, unix, pendingintent);
                } else {
                    Log.d("DEFAULT", "App does not have permission to schedule exact alarms");
                }
    }

    @Nullable
    @Override public IBinder onBind(Intent intent) { return null; }
}
