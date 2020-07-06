package com.indra.android.updatingindia;

public class scan_history_info {

    private String location_name,qr_text;
    private long scan_count;

    public String getLocation_name() {
        return location_name;
    }


    public  scan_history_info(String mlocation_name) {
        location_name=mlocation_name;
        scan_count=10000;
    }

    public  scan_history_info(String mlocation_name,long mscan_count,String mqr_text) {
        location_name=mlocation_name;
        scan_count=mscan_count;
        qr_text=mqr_text;
    }

    public long getScan_count() {
        return scan_count;
    }

    public String getQr_text() {
        return qr_text;
    }
}
