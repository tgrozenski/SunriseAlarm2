
package com.example.sunrisealarm20;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.CheckBox;
public class custom_alarm extends AppCompatActivity {

    Alarm myAlarm;

    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.custom_alarm);


//        //hour & minute
//        Button button = findViewById(R.id.schedule_alarm);
//        button.setOnClickListener(v -> {
//            TimePicker timepicker = findViewById(R.id.alarm_time);
//            myAlarm.hour = timepicker.getHour();
//            myAlarm.minute = timepicker.getMinute();
//        });
//
//        //name
//        Button sub_button = findViewById(R.id.submit_alarm_button);
//        sub_button.setOnClickListener(v -> {
//            EditText alarm_name =  findViewById(R.id.createalarm_title);
//            myAlarm.name =  alarm_name.getText().toString();
//
//            //hide Keyboard
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(alarm_name.getWindowToken(), 0);
//        });
//        //is recurring
//        CheckBox checkBox = findViewById(R.id.recurring_checkbox);
//        checkBox.setOnClickListener(v -> {
//            if(checkBox.isChecked()) {
//                myAlarm.isRecurring = true;
//            }
//            else {
//                myAlarm.isRecurring = true;
//            }
//        });

    }


}