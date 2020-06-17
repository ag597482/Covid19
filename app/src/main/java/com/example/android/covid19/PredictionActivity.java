package com.example.android.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.covid19.Utils1.LOG_TAG;
import static java.lang.String.format;


public class PredictionActivity extends AppCompatActivity {


    int flag=0;

    private static final String USGS_REQUEST_URL =
            "https://api.rootnet.in/covid19-in/hospitals/medical-colleges";


    public String location_slected,location_detail;

    ProgressBar progressBar;
    TextView textView,active,deaths,rec,dttt,dtta,dttr,dttd,pred,tday,place,stname;
    ImageView aro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);




        aro=findViewById(R.id.arrow);


        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);


        dttt = (TextView)findViewById(R.id.dtotal);
        dtta = (TextView)findViewById(R.id.dactive);
        dttr = (TextView)findViewById(R.id.drecovered);
        dttd = (TextView)findViewById(R.id.ddeaths);


        place=(TextView)findViewById(R.id.place);



        location_slected = getIntent().getData().toString();


        location_detail = QueryUtils.detacard.get(location_slected).getLocation_detail();


        String cardclicked = getIntent().getData().toString();
        place.setText(cardclicked);


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


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011-23978046"));
                startActivity(intent);
            }
        });

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


}
