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
public class medi_card_addapter extends ArrayAdapter<medi_info_card>  {

    private static final String LOG_TAG = medi_card_addapter.class.getSimpleName();


    public medi_card_addapter(Activity context, ArrayList<medi_info_card> medi_card) {
        super(context, 0, medi_card);
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
                    R.layout.medi_list_item, parent, false);
        }
        // Get the {@link AndroidFlavor} object located at this position in the list
        medi_info_card currentAndroidFlavor = getItem(position);

        TextView hospital_name = (TextView) listItemView.findViewById(R.id.hospital_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        hospital_name.setText(currentAndroidFlavor.getHospital_name());


        TextView bed = (TextView) listItemView.findViewById(R.id.bed_count);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        bed.setText(currentAndroidFlavor.getBed());


        TextView city = (TextView) listItemView.findViewById(R.id.city_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        city.setText(currentAndroidFlavor.getCity());

        return listItemView;

    }

}
