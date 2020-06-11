package com.example.android.covid19;

import com.google.android.gms.common.internal.Objects;

public class Feedback {


    private String name,mobile,message;


    public Feedback()
    {

    }

    public Feedback(String n,String mo,String m)
    {
        name=n;
        mobile=mo;
        message=m;
    }

    public String getName(){return name; }

    public String getMobile(){return  mobile; }

    public String getMessage(){return message; }





}
