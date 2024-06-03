package com.example.sunrisealarm20;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //build Channel
        CharSequence name = "CHANNEL NAME";
        String description = "This is an Alarm Channel";
        String CHANNEL_ID = "My Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}