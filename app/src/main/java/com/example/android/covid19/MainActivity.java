package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button ct,st,dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> earthquakes = new ArrayList<>();
        earthquakes.add("San Francisco");
        earthquakes.add("London");
        earthquakes.add("Tokyo");
        earthquakes.add("Mexico City");
        earthquakes.add("Moscow");
        earthquakes.add("Rio de Janeiro");
        earthquakes.add("Paris");
        earthquakes.add("San Francisco");
        earthquakes.add("London");
        earthquakes.add("Tokyo");
        earthquakes.add("Mexico City");
        earthquakes.add("Moscow");
        earthquakes.add("Rio de Janeiro");
        earthquakes.add("Paris");


        // Find a reference to the {@link ListView} in the layout
        Spinner earthquakeListView = (Spinner) findViewById(R.id.location);

        // Create a new {@link ArrayAdapter} of earthquakes
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);




        // Create an ArrayList of AndroidFlavor objects
        ArrayList<info_card> androidFlavors = new ArrayList<info_card>();
        androidFlavors.add(new info_card("Donut", "1.6"));
        androidFlavors.add(new info_card("Eclair", "2.0-2.1"));
        androidFlavors.add(new info_card("Froyo", "2.2-2.2.3"));
        androidFlavors.add(new info_card("GingerBread", "2.3-2.3.7"));
        androidFlavors.add(new info_card("Honeycomb", "3.0-3.2.6"));
        androidFlavors.add(new info_card("Ice Cream Sandwich", "4.0-4.0.4"));
        androidFlavors.add(new info_card("Jelly Bean", "4.1-4.3.1"));
//        androidFlavors.add(new info_card("KitKat", "4.4-4.4.4", R.drawable.kitkat));
//        androidFlavors.add(new info_card("Lollipop", "5.0-5.1.1", R.drawable.lollipop));
//        androidFlavors.add(new info_card("Marshmallow", "6.0-6.0.1", R.drawable.marshmallow));

        // Create an {@link AndroidFlavorAdapter}, whose data source is a list of
        // {@link AndroidFlavor}s. The adapter knows how to create list item views for each item
        // in the list.
        Location_card_addapter flavorAdapter = new Location_card_addapter(this, androidFlavors);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);


    }
}
