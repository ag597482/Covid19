package com.example.android.covid19;

public class info_card {

    private String mVersionName;
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
