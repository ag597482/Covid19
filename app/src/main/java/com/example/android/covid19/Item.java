package com.example.android.covid19;

public class Item {

    String place;
    int total,active,recovered,deaths;
    public Item(String p,int t,int a,int r,int d)
    {
        place=p;
        total=t;
        active=a;
        recovered=r;
        deaths=d;
    }
    String getPlace()
    {
        return place;
    }
    int getTotal()
    {
        return total;
    }
    int getActive()
    {
        return active;
    }
    int getRecovered()
    {
        return recovered;
    }
    int getDeaths()
    {
        return deaths;
    }
}
