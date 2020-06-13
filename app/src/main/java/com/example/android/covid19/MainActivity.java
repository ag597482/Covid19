package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/state_district_wise.json";
    TextView textView,active,deaths,rec;
    ProgressBar progressBar;
    Spinner spinner;
    ListView listView;
    SearchView searchView;
    ImageView sort_list;

    String current_state;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        spinner = (Spinner)findViewById(R.id.spinner);
        listView = (ListView)findViewById(R.id.list);
        searchView =(SearchView)findViewById(R.id.serch);

        current_state="India";






        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


        //spinner sort
        sort_list = findViewById(R.id.sort_list);

        sort_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(MainActivity.this , v);
                    popup.setOnMenuItemClickListener(MainActivity.this);

                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.sort, popup.getMenu());
                    popup.show();
            }
        });

      //  int res = ContextCompat.getDrawable(,R.drawable.rectangular);


//        ArrayAdapter<String> sort_list_adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, sort_str);
//        sort_list.setAdapter(sort_list_adapter);
//        sort_list.getDropDownVerticalOffset();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String tutorialsName = parent.getItemAtPosition(position).toString();
                current_state=tutorialsName;

               // ArrayList<String> distictsinstate = new ArrayList<String>(QueryUtils.disArray);

                final ArrayList<info_card> num= new ArrayList<info_card>(QueryUtils.all.get(tutorialsName));

                if(tutorialsName.equals("India-Districts"))
                {

                }
                else {
                    num.add(0, total_sum(num, tutorialsName));
                }

                final Location_card_addapter location_card_addapter=new Location_card_addapter(MainActivity.this,num);

               // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,distictsinstate);
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName + " " + QueryUtils.all.get(tutorialsName).size() ,Toast.LENGTH_LONG).show();
                listView.setAdapter(location_card_addapter);

                listView.setTextFilterEnabled(true);





                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        ArrayList<info_card> results= new ArrayList<info_card>();

                        for(int i=0;i<num.size();i++)
                        {
                            if(num.get(i).getLocation_name().toLowerCase().contains(newText.toLowerCase()))
                            {
                                results.add(num.get(i));
                            }
                        }
                        Location_card_addapter resadapter=new Location_card_addapter(MainActivity.this,results);
                        listView.setAdapter(resadapter);



                        return true;
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(MainActivity.this,PredictionActivity.class);
                        info_card infoCard=(info_card)parent.getItemAtPosition(position);
                        intent.setData(Uri.parse(infoCard.getLocation_name()));
                        startActivity(intent);
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.graphs:
                Intent intent =new Intent(MainActivity.this,GraphActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedback:
                Intent intent1 =new Intent(MainActivity.this,FeedbackActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public info_card total_sum(ArrayList<info_card>arr,String name)
    {

        int tc=0,tr=0,td=0,ta=0;
        for(int i=0;i<arr.size();i++)
        {
            tc=tc+arr.get(i).getLocation_total_cases();
            tr=tr+arr.get(i).getLocation_recovery();
            td=td+arr.get(i).getLocation_death();
            ta=ta+arr.get(i).getLocation_active();
        }
        textView.setText(String.valueOf(tc));
        active.setText(String.valueOf(ta));
        deaths.setText(String.valueOf(td));
        rec.setText(String.valueOf(tr));
        return new info_card(name,"",tc,ta,tr,td);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alpha:
                update_list("a");
                return true;
            case R.id.tot:
                update_list("b");
                return true;
            case R.id.rec:
                update_list("c");
                return true;
            case R.id.det:
                update_list("d");
                return true;
            case R.id.act:
                update_list("e");
                return true;
            case R.id.rrate:
                update_list("f");
                return true;
            case R.id.drate:
                update_list("g");
                return true;
            default:
                return false;
        }
    }

    private void update_list(final String s) {
        final ArrayList<info_card> num= new ArrayList<info_card>(QueryUtils.all.get(current_state));

        if(current_state.equals("India-Districts"))
        {

        }
        else {
            num.add(0, total_sum(num,current_state));
        }

        Collections.sort(num, new Comparator<info_card>() {
            @Override
            public int compare(info_card o1, info_card o2) {
                if(s.equals("a"))
                {
                    return o2.getLocation_name().compareTo(o2.getLocation_name());
                }
                else if(s.equals("b"))
                {
                    return Integer.valueOf(o1.getLocation_total_cases()).compareTo(o2.getLocation_total_cases());
                }
                else if(s.equals("c"))
                {
                    return Integer.valueOf(o1.getLocation_recovery()).compareTo(o2.getLocation_recovery());
                }
                else if(s.equals("d"))
                {
                    return Integer.valueOf(o1.getLocation_death()).compareTo(o2.getLocation_death());
                }
                else if(s.equals("e"))
                {
                    return Integer.valueOf(o1.getLocation_active()).compareTo(o2.getLocation_active());
                }
                else if(s.equals("f"))
                {
                    return Double.valueOf(o1.getrecoveryrate()).compareTo(o2.getrecoveryrate());
                }
                else if(s.equals("g"))
                {
                    return Double.valueOf(o1.getdeathrate()).compareTo(o2.getdeathrate());
                }
                return 0;

            }
        });

        if(s.equals("a"))
        {
        }
        else
        {
            Collections.reverse(num);
        }

        final Location_card_addapter location_card_addapter=new Location_card_addapter(MainActivity.this,num);
        listView.setAdapter(location_card_addapter);


    }

//    public void Update_location_card(ArrayList<info_card> git)
//    {
//        total_sum(git);
//        Location_card_addapter flavorAdapter = new Location_card_addapter(this, git);
//
//        // Get a reference to the ListView, and attach the adapter to the listView.
//        ListView listView = (ListView) findViewById(R.id.list);
//        listView.setAdapter(flavorAdapter);
//    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<String> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;

        }


        @Override
        protected void onPostExecute(List<String> data) {
            if (data != null && !data.isEmpty()) {
               // textView.setText(data.toString());
                textView.setText(String.valueOf(QueryUtils.total1));
                active.setText(String.valueOf(QueryUtils.active));
                deaths.setText(String.valueOf(QueryUtils.deaths));
                rec.setText(String.valueOf(QueryUtils.recovered));
                ArrayList<String> states = new ArrayList<>(QueryUtils.all.keySet());
                Collections.sort(states);

                states.add(0,"India");
                states.add(1,"India-Districts");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.test,states);
                arrayAdapter.setDropDownViewResource(R.layout.test);

                spinner.setAdapter(arrayAdapter);


                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
