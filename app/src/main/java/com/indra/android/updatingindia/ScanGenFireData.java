package com.indra.android.updatingindia;

public class ScanGenFireData {


    private String location_name="nothing";
    private String user_email,qr_text;
    private long qr_num,total_scan_Count=0,present_scan_count=0,total_covid_cases_in_last_7_days=0;

    public String getQr_text() {
        return qr_text;
    }

    public long getTotal_scan_Count() {
        return total_scan_Count;
    }

    public void setTotal_scan_Count(long total_scan_Count) {
        this.total_scan_Count = total_scan_Count;
    }

    public long getPresent_scan_count() {
        return present_scan_count;
    }

    public void setPresent_scan_count(long present_scan_count) {
        this.present_scan_count = present_scan_count;
    }

    public long getTotal_covid_cases_in_last_7_days() {
        return total_covid_cases_in_last_7_days;
    }

    public void setTotal_covid_cases_in_last_7_days(long total_covid_cases_in_last_7_days) {
        this.total_covid_cases_in_last_7_days = total_covid_cases_in_last_7_days;
    }

    public void setQr_text(String qr_text) {
        this.qr_text = qr_text;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }


    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public long getQr_num() {
        return qr_num;
    }

    public void setQr_num(long qr_num) {
        this.qr_num = qr_num;
    }


}
