package com.example.android.covid19;

public class info_card {

    private String location_name;
    private String location_detail;
    private int location_total_cases;
    private int location_active;
    private int location_recovery;
    private int location_death;
    private int dela_confirmed;
    private int delta_recover;
    private int delta_deaths;

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

    public info_card(String name,int c,int r,int d,int tc,int ta,int tr,int td)
    {
        location_name=name;
        dela_confirmed=c;
        delta_recover=r;
        delta_deaths=d;
        location_active=ta;
        location_death=td;
        location_recovery=tr;
        location_total_cases=tc;

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

    public int getDela_confirmed(){
        return dela_confirmed;
    }
    public int getDelta_recover(){return delta_recover; }
    public int getDelta_deaths(){ return delta_deaths; }

}
