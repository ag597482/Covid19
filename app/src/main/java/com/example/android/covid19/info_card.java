package com.example.android.covid19;

public class info_card {

    private String location_name;
    private String location_detail;
    private int location_total_cases;
    private int location_active;
    private int location_recovery;
    private int location_death;

    private int mImageResourceId;

    public info_card(String vlocation_name, String vlocation_detail, int vlocation_total_cases, int vlocation_active, int vlocation_recovery,int vlocation_death)
    {
        location_name = vlocation_name;
        location_detail = vlocation_detail;
        location_active=vlocation_active;
        location_death=vlocation_death;
        location_recovery=vlocation_recovery;
        location_total_cases=vlocation_total_cases;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getLocation_detail() {
        return location_detail;
    }

    public int getLocation_total_cases() {
        return location_total_cases;
    }
    public int getLocation_active() {
        return location_active;
    }
    public int getLocation_recovery() {
        return location_recovery;
    }
    public int getLocation_death() {
        return location_death;
    }

}
