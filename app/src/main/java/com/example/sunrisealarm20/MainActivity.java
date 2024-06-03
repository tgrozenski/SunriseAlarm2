package com.example.sunrisealarm20;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getLocation();

        //TimePickers
        NumberPicker before_picker = findViewById(R.id.before_selector);
        before_picker.setMinValue(0);
        before_picker.setMaxValue(30);

        NumberPicker after_picker = findViewById(R.id.after_selector);
        after_picker.setMinValue(0);
        after_picker.setMaxValue(30);

        //Buttons & Switch
        Button change_button = findViewById(R.id.change_button);
        change_button.setOnClickListener(v -> {
            try {
                this.setCustom();
                this.setAlert();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        SwitchCompat switch1 = findViewById(R.id.switch1);
        Intent myintent = new Intent(MainActivity.this, notificationService.class);

        switch1.setOnClickListener(v -> {
            if(!alarm.active){
                Log.d("DEFAULT", "SERVICE SHOULD BE STARTED" + alarm.formatTime());
                myintent.putExtra("Unix", alarm.time_to_mili());
                myintent.putExtra("cmd", "Start");
                myintent.putExtra("AlarmTime", alarm.formatTime());
                this.startForegroundService(myintent);
            }
            else {
                myintent.putExtra("cmd", "Stop");
                this.startForegroundService(myintent);
            }
            alarm.active = switch1.isChecked();
            Log.d("DEFAULT", "STATUS: " + alarm.active);
            //Start Alarm Activity
        });
        Button locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Try reopening the app after using location services in another app", Toast.LENGTH_LONG).show();
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private void setAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are You Sure You Want to change to " + alarm.formatTime());
        builder.setTitle("Alert!");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            //do Work to Schedule Alarm
            TextView tv = findViewById(R.id.alarm_time);
            tv.setText(alarm.formatTime());
            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setCustom() {
        alarm.hour = alarm.sunrise_hour;
        alarm.minute = alarm.sunrise_minute;
        NumberPicker numberPicker = findViewById(R.id.before_selector);
        int beforeSunrise = numberPicker.getValue();
        NumberPicker numberPicker2 = findViewById(R.id.after_selector);
        int afterSunrise = numberPicker2.getValue();

        if (alarm.minute - beforeSunrise >= 0) {
            alarm.minute = alarm.minute - beforeSunrise;
        } else {
            alarm.minute = alarm.minute - beforeSunrise + 60;
            alarm.hour--;
        }
        if (alarm.minute + afterSunrise >= 60) {
            alarm.minute = alarm.minute + afterSunrise - 60;
            alarm.hour++;
        } else {
            alarm.minute = alarm.minute + afterSunrise;
        }
    }
    public void getLocation() {
        Log.d("DEFAULT", "getLocationCalled");
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.d("default", "Precise location access granted");
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d("default", "Approximate location access granted");
                            } else {
                                Log.d("default", "No Location access granted");
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null) {
                    // Logic to handle location object
                    saveLocation(location);
                    Log.d("DEFAULT", "Location:" + location);

                } else {
                    Log.d("DEFAULT", "Location is null");
                    MainActivity.this.getLocation();
                    Toast.makeText(MainActivity.this, "Try again after using location services in another app", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void saveLocation( android.location.Location location ) {
        TimeZone timezone = TimeZone.getDefault();
        Location currentLocation = new Location(location.getLatitude(), location.getLongitude(), timezone);
        TextView textView = findViewById(R.id.time_zone);
        textView.setText(currentLocation.timezone.getDisplayName());
        TextView otherTV = findViewById(R.id.sunrise_time);
        String nextSunrise = currentLocation.getNextSunrise();
        Log.d("DEFAULT", "SUNRISE IN SAVE LOCATION" + nextSunrise);
        otherTV.setText(nextSunrise);

        // Handle setting sunrise
        Log.d("DEFAULT", "Sunrise: " + nextSunrise);
        alarm = new Alarm(nextSunrise);
        TextView TV = findViewById(R.id.alarm_time);
        TV.setText(alarm.formatTime());
    }
}