package com.indra.android.updatingindia;

public class Report {


    String id,reportno,details;

    public Report(){

    }

    public Report(String i,String n,String d)
    {
        id=i;
        reportno=n;
        details=d;
    }

    public String getId(){return  id;}

    public String getDetails() {
        return details;
    }

    public String getReportno() {
        return reportno;
    }

}
