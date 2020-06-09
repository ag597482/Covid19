/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.covid19;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
* {@link AndroidFlavorAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link AndroidFlavor} objects.
* */
public class Location_card_addapter extends ArrayAdapter<info_card> {

    private static final String LOG_TAG = Location_card_addapter.class.getSimpleName();


    public Location_card_addapter(Activity context, ArrayList<info_card> location_card) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, location_card);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        info_card currentAndroidFlavor = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView location_name = (TextView) listItemView.findViewById(R.id.location_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        location_name.setText(currentAndroidFlavor.getLocation_name());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView location_detail = (TextView) listItemView.findViewById(R.id.location_detail);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        location_detail.setText(currentAndroidFlavor.getLocation_detail());

        TextView location_active = (TextView) listItemView.findViewById(R.id.location_active);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        location_active.setText(String.valueOf(currentAndroidFlavor.getLocation_active()));

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView location_death = (TextView) listItemView.findViewById(R.id.location_death);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        location_death.setText(String.valueOf(currentAndroidFlavor.getLocation_death()));

        TextView location_recovery = (TextView) listItemView.findViewById(R.id.location_recovery);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        location_recovery.setText(String.valueOf(currentAndroidFlavor.getLocation_recovery()));

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView location_total_cases = (TextView) listItemView.findViewById(R.id.location_total_cases);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        location_total_cases.setText(String.valueOf( currentAndroidFlavor.getLocation_total_cases()));
        return listItemView;
    }

}
