package com.example.sunrisealarm20;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;

class Location {
    public double Latitude;
    public double Longitude;
    public TimeZone timezone;

    public Location(double lat, double lon, TimeZone timezone) {
        this.Latitude = lat;
        this.Longitude = lon;
        this.timezone = timezone;
    }

    public String getNextSunrise() {

        com.luckycatlabs.sunrisesunset.dto.Location sunrise_location = new com.luckycatlabs.sunrisesunset.dto.Location(this.Latitude + "", this.Longitude + "");
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(sunrise_location, timezone);

        Date dt = new Date();
        Calendar c = Calendar.getInstance(this.timezone);
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        c.setTime(dt);

        Log.d("DEFAULT", "Date" + c.getTime() + "Sunrise" + calculator.getOfficialSunriseForDate(c));

        return calculator.getOfficialSunriseForDate(c);
    }

}