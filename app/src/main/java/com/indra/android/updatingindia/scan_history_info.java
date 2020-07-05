package com.indra.android.updatingindia;

public class scan_history_info {

    private String location_name;
    private int scan_count;

    public String getLocation_name() {
        return location_name;
    }


    public  scan_history_info(String mlocation_name) {
        location_name=mlocation_name;
        scan_count=10000;
    }

    public  scan_history_info(String mlocation_name,int mscan_count) {
        location_name=mlocation_name;
        scan_count=mscan_count;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getScan_count() {
        return scan_count;
    }

    public void setScan_count(int scan_count) {
        this.scan_count = scan_count;
    }
}
