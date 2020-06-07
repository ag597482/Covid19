package com.example.android.covid19;

public class info_card {

    private String mVersionName;

    // Android version number (e.g. 2.3-2.7, 3.0-3.2.6, 4.0-4.0.4)
    private String mVersionNumber;

    private int mImageResourceId;

    public info_card(String vName, String vNumber)
    {
        mVersionName = vName;
        mVersionNumber = vNumber;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public String getVersionNumber() {
        return mVersionNumber;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }
}
