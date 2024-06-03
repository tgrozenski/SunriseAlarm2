package com.example.sunrisealarm20;

import android.Manifest;
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
    int sunriseHour = 7;
    int sunriseMinute = 32;
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getLocation();
        alarm = new Alarm(sunriseHour, sunriseMinute, "0", true);

        TextView textView = findViewById(R.id.alarm_time);
        textView.setText(alarm.formatTime());

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
        switch1.setOnClickListener(v -> {
            alarm.isRecurring = switch1.isActivated();
        });
        Button locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Try reopening the app after using location services in another app", Toast.LENGTH_LONG).show();
        });
    }

    private void setAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are You Sure You Want to change to " + alarm.formatTime());
        builder.setTitle("Alert!");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            //do Work to Schedule Alarm

            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            alarm.minute = sunriseMinute;
            alarm.hour = sunriseHour;
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setCustom() {

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
        } else {
            alarm.minute = alarm.minute + afterSunrise;
        }
    }
    public void getLocation() {
        final Location[] currentLocation = {null};
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
        otherTV.setText(currentLocation.getNextSunrise());
    }

}