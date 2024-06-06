package com.example.sunrisealarm20;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_screen_layout);

        Intent intent = new Intent(AlarmScreen.this, MainActivity.class);
        intent.putExtra("State", 1);

        Button stop = findViewById(R.id.stop_sound);
        stop.setOnClickListener(v->{
            //logic here for stopping the service
            Intent serviceInt = new Intent(getApplicationContext(), ringEvent.class);
            stopService(serviceInt);

            this.startActivity(intent);
        });

    }
}
