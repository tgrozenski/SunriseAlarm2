package com.example.sunrisealarm20;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.CombinedVibration;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ringEvent extends Service {
    private MediaPlayer mediaplayer;
    private VibratorManager vibratorManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaplayer = MediaPlayer.create(this, R.raw.queen_sound);
        mediaplayer.setLooping(true);
        mediaplayer.start();
        Log.d("DEFAULT", "Alarm should be ringing now!");

        vibratorManager= (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        long[] timings = new long[] { 50, 50, 100, 50, 50 };
        int[] amplitudes = new int[] { 64, 128, 255, 128, 64 };
        VibrationEffect effect = VibrationEffect.createWaveform(timings, amplitudes, 1);
        CombinedVibration combinedVibration = CombinedVibration.createParallel(effect);
        vibratorManager.vibrate(combinedVibration);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1 = new Intent(getApplicationContext(), AlarmScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);

        String CHANNEL_ID = "My Channel";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("Good Morning")
                .setContentText("Click the Button to Silence the Alarm")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(ringEvent.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ERROR", "Permission not granted");
        }
        //notify
        NotificationManagerCompat.from(this).notify(1, builder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        vibratorManager.cancel();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
