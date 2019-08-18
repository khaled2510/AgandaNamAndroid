package com.henallux.khal.smartcity.activity.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertionDate {

    // Convertion date String vers String
    public String convertDate(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date datetime = null;
        try
        {
            datetime = sdf.parse(date);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a");
        return formatter.format(datetime);
    }

    // Convertion date vers String yyyy-MM-dd
    public String convertDateToString(Date date){
        String dateString;
        dateString = String.valueOf(date.getYear()) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        return dateString;
    }
}
