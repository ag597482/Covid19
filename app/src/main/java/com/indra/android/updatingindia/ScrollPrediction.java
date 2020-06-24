package com.indra.android.updatingindia;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrollPrediction extends AppCompatActivity {




    public String location_slected,location_detail;

    ProgressBar progressBar;
    TextView textView,active,deaths,rec,dttt,dtta,dttr,dttd,pred,tday,place,stname,helpline;
    ImageView aro;


    private static final String USGS_REQUEST_URL =
            "https://api.rootnet.in/covid19-in/hospitals/medical-colleges";

    static String [] state= {"India","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
            "Dadra and Nagar Haveli", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"} ;

    static String [] hno= {"011-23978046","0866-2410978", "9436055743", "6913347770", "104", "9779558282", "104", "104", "8558893911", "104", "01912520982", "104", "104", "0471-2552056", "104", "020-26127394", "3852411668", "108", "102", "7005539653", "9439994859", "104", "0141-2225624", "104", "044-29510500", "104", "0381-2315879", "104", "18001805145", "1800313444222", "03192-232102", "104",
            "104", "104", "011-22307145", "104", "104"} ;

    HashMap<String,String> shno;


    String str;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_prediction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // toolbar.setVisibility(View.GONE);


        shno=new HashMap<>();
        for(int i=0;i<state.length;i++)
        {
            shno.put(state[i],hno[i]);
        }

        aro=findViewById(R.id.arrow);


        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);


        dttt = (TextView)findViewById(R.id.dtotal);
        dtta = (TextView)findViewById(R.id.dactive);
        dttr = (TextView)findViewById(R.id.drecovered);
        dttd = (TextView)findViewById(R.id.ddeaths);
        stname =findViewById(R.id.sname);


        place=(TextView)findViewById(R.id.place);
        progressBar=findViewById(R.id.progres);




        location_slected = getIntent().getData().toString();


        location_detail = QueryUtils.detacard.get(location_slected).getLocation_detail();


        String no="104";
        if(shno.containsKey(location_detail))
            no=shno.get(location_detail);

        helpline=findViewById(R.id.shn);
        helpline.setText("State Helpline No: "+no);


        String cardclicked = getIntent().getData().toString();
        place.setText(cardclicked);

        stname.setText(location_detail);

        info_card infoCard=QueryUtils.detacard.get(cardclicked);
        textView.setText(infoCard.getLocation_total_cases()+ "");
        dttt.setText("" +infoCard.getDela_confirmed());


        final int currr=infoCard.getLocation_total_cases();





        active.setText(infoCard.getLocation_active()+ "");
        dtta.setText("" + (infoCard.getDela_confirmed()-infoCard.getDelta_recover()-infoCard.getDelta_deaths()) );
        if((infoCard.getDela_confirmed()-infoCard.getDelta_recover()-infoCard.getDelta_deaths())<0)
        {
            aro.setVisibility(View.INVISIBLE);
        }

        rec.setText(infoCard.getLocation_recovery()+ "");
        dttr.setText("" +infoCard.getDelta_recover());

        deaths.setText(infoCard.getLocation_death()+ "");
        dttd.setText("" +infoCard.getDelta_deaths());

        TextView recra = (TextView)findViewById(R.id.rra);
        TextView ddra = (TextView)findViewById(R.id.dra);

        double rra=(double)infoCard.getLocation_recovery()/infoCard.getLocation_total_cases();

        double dra=(double)infoCard.getLocation_death()/infoCard.getLocation_total_cases();

        recra.setText("Recovery rate : " + String.format("%.2f", rra*100) + "%");
        ddra.setText("Mortality rate : " + String.format("%.2f", dra*100)+ "%");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 str="tel:104";
                if(shno.containsKey(location_detail));
                str="tel:"+shno.get(location_detail);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(str));
                startActivity(intent);
            }
        });


        if(Utils1.state_medi_info.size()==0) {
            ScrollPrediction.EarthquakeAsyncTask task = new ScrollPrediction.EarthquakeAsyncTask();
            task.execute(USGS_REQUEST_URL);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            medi_card_addapter flavorAdapter;
            ArrayList<medi_info_card> required_list = new ArrayList<>();

            if(Utils1.state_medi_info.containsKey(location_detail))
            required_list = new ArrayList<>(Utils1.state_medi_info.get(location_detail));





            //flavorAdapter = new medi_card_addapter(ScrollPrediction.this, required_list);

            int tb = 0;
            for (int i = 0; i < required_list.size(); i++) {
                tb = tb + Integer.valueOf(required_list.get(i).getBed());
            }
         //   TextView total_hospitals = findViewById(R.id.hospital_value);

         //   total_hospitals.setText("No. of Hospitals in State  : "+String.valueOf(required_list.size()));

           // TextView total_beds = findViewById(R.id.bed_value);
          //  total_beds.setText("No. of beds in State          : "+String.valueOf(tb));


            required_list.add(new medi_info_card("Total No. of Hospials ("+required_list.size()+")","Total No. of Beds ("+tb+")",location_detail));

            RecyclerView listView = findViewById(R.id.list_pre);

            listView.setLayoutManager(new LinearLayoutManager(ScrollPrediction.this));

            madapter adapter = new madapter(ScrollPrediction.this,required_list);


            listView.setAdapter(adapter);


        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pred, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.insights:
                openDialog();
                //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<medi_info_card>> {

        protected ArrayList<medi_info_card> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<medi_info_card> result = Utils1.fetchEarthquakeData(urls[0]);
            return result;
        }


        protected void onPostExecute(ArrayList<medi_info_card> result) {


                if (Utils1.state_medi_info.containsKey(location_detail)) {
                    progressBar.setVisibility(View.GONE);
                    medi_card_addapter flavorAdapter;
                    ArrayList<medi_info_card> required_list = new ArrayList<>();

                    required_list = new ArrayList<>(Utils1.state_medi_info.get(location_detail));





                   // flavorAdapter = new medi_card_addapter(ScrollPrediction.this, required_list);

                    int tb = 0;
                    for (int i = 0; i < required_list.size(); i++) {
                        tb = tb + Integer.valueOf(required_list.get(i).getBed());
                    }
                    //   TextView total_hospitals = findViewById(R.id.hospital_value);

                    //   total_hospitals.setText("No. of Hospitals in State  : "+String.valueOf(required_list.size()));

                    // TextView total_beds = findViewById(R.id.bed_value);
                    //  total_beds.setText("No. of beds in State          : "+String.valueOf(tb));



                    required_list.add(new medi_info_card("Total No. of Hospials ("+required_list.size()+")","Total No. of Beds ("+tb+")",location_detail));

                    RecyclerView listView = findViewById(R.id.list_pre);

                    listView.setLayoutManager(new LinearLayoutManager(ScrollPrediction.this));

                    madapter adapter = new madapter(ScrollPrediction.this,required_list);


                    listView.setAdapter(adapter);


                } else {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ScrollPrediction.this, "Hospital Data not found", Toast.LENGTH_SHORT).show();
                }
            }


    }
}
