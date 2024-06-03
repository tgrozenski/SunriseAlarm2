package com.example.sunrisealarm20;

import android.util.Log;

import java.util.Calendar;

class Alarm {
   int hour;
   int minute;
   int sunrise_hour;
   int sunrise_minute;
   boolean active;
   double unix;
   //Constructor
    public Alarm(String sunrise) {
        string_to_time(sunrise);
    }
    public double time_to_mili() {
        Calendar calendar = Calendar.getInstance();
        long current = ((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE)) * 60000;
        long timeinmil = ((long) this.hour * 60 + (long) this.minute) * 60000;
        Log.d("Buttons", timeinmil + "<-time in milliseconds");
        Log.d("Buttons", current + "<-current time");
        if (timeinmil > current) {
            unix = timeinmil - current;
        }
        else {
            unix = ((24 * 60) * 60000) - (current - timeinmil);
        }
        return unix;
    }
    public String formatTime() {
        String time = "";
        if(minute != 0 || hour != 0) {
            if (minute < 10) {
                time = hour + ":0" + minute;
            } else {
                time = hour + ":" + minute;
            }
        }
        return time;
    }
    public void string_to_time(String officialSunrise) {
        char[] arr = officialSunrise.toCharArray();
        int alarm_hour = 0;
        int alarm_minute = 0;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                alarm_hour += Character.getNumericValue(arr[i]) * 10;
            } else {
                alarm_hour += Character.getNumericValue(arr[i]);
            }
        }
        for (int i = 3; i < 5; i++) {
            if (i == 3) {
                alarm_minute += Character.getNumericValue(arr[i]) * 10;
            } else {
                alarm_minute += Character.getNumericValue(arr[i]);
            }
        }

        this.hour = alarm_hour;
        this.minute = alarm_minute;
        this.sunrise_hour = alarm_hour;
        this.sunrise_minute = alarm_minute;
    }

}
