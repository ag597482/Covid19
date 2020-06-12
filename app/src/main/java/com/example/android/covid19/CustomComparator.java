package com.example.android.covid19;

import java.util.Comparator;

public class CustomComparator implements Comparator<info_card> {
    @Override
    public int compare(info_card o1, info_card o2) {
        return o1.getLocation_name().compareTo(o2.getLocation_name());
    }
}
