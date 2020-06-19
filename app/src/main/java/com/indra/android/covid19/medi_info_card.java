package com.indra.android.covid19;

public class medi_info_card  {

    String hospital_name,bed,city;
    public medi_info_card(String hospital_namet, String bedt, String cityt)
    {
        hospital_name=hospital_namet;
        bed=bedt;
        city=cityt;
    }
    public String getHospital_name(){ return hospital_name; }
    public String getBed(){ return bed; }
    public String getCity(){ return city; }

}
