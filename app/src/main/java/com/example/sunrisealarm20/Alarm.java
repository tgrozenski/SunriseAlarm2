package com.example.sunrisealarm20;

import android.util.Log;

import java.util.Calendar;

class Alarm {
   int hour;
   int minute;
   boolean active;

   double unix;
   String ID;
   boolean isRecurring;
   //Constructor
    public Alarm(int hr, int min, String id, boolean recurring) {
        this.hour = hr;
        this.minute = min;
        this.ID = id;
        this.isRecurring = recurring;
        this.unix = time_to_mili(hour, minute);
        this.active = false;
    }
    public static long time_to_mili(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long current = ((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE)) * 60000;
        long timeinmil = ((long) hour * 60 + (long) minute) * 60000;
        Log.d("Buttons", timeinmil + "<-time in milliseconds");
        Log.d("Buttons", current + "<-current time");
        if (timeinmil > current) {
            return timeinmil - current;
        }
        else {
            return ((24 * 60) * 60000) - (current - timeinmil);
        }
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


}
