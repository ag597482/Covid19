package com.example.android.covid19;

import android.util.Log;

import java.util.Comparator;

public class CustomComparator implements Comparator<info_card> {

    public static final String LOG_TAG = CustomComparator.class.getSimpleName();

    String [] str={"Alphabetical","Total Case","Recovery Cases","Death Cases","Active Cases","Recovery Rate","Death Rate"};
    public int compare1(info_card o1, info_card o2,String type) {
        Log.i(LOG_TAG,"0"+str[0]);
        if(type.equals(str[0]))
        return o1.getLocation_name().compareTo(o2.getLocation_name());

        Log.i(LOG_TAG,"1"+str[1]);

        if(type.equals(str[1]))
            return o2.getLocation_total_cases()-o1.getLocation_total_cases();


        Log.i(LOG_TAG,"2"+str[2]);

        if(type.equals(str[2]))
            return o2.getLocation_recovery()-o1.getLocation_recovery();

        Log.i(LOG_TAG,"3"+str[3]);

        if(type.equals(str[3]))
            return o2.getLocation_death()-o1.getLocation_death();

        Log.i(LOG_TAG,"4"+str[4]);

        if(type.equals(str[4]))
            return o2.getLocation_active()-o1.getLocation_active();

        Log.i(LOG_TAG,"5"+str[5]);

        if(type.equals(str[5]))
            return o2.getLocation_active()-o1.getLocation_active();

        Log.i(LOG_TAG,"1"+str[1]);

        if(type.equals(str[6]))
            return o2.getLocation_active()-o1.getLocation_active();

        Log.i(LOG_TAG,"1"+str[1]);

        return o1.getLocation_name().compareTo(o2.getLocation_name());

    }

    @Override
    public int compare(info_card o1, info_card o2) {
        return compare1(o1,o2,MainActivity.sort_by);
    }
}
