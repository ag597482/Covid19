package com.indra.android.updatingindia;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

    public class ScanHistoryAddapter extends ArrayAdapter<scan_history_info> {

        private static final String LOG_TAG = com.indra.android.updatingindia.Location_card_addapter.class.getSimpleName();


        public ScanHistoryAddapter(Activity context, ArrayList<scan_history_info> location_card) {
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
                        R.layout.scan_card_list, parent, false);
            }

            // Get the {@link AndroidFlavor} object located at this position in the list
            scan_history_info currentAndroidFlavor = getItem(position);

            // Find the TextView in the list_item.xml layout with the ID version_name
            TextView location_name = (TextView) listItemView.findViewById(R.id.location_name_scan);

            location_name.setText(currentAndroidFlavor.getLocation_name());

            TextView scan_count = (TextView) listItemView.findViewById(R.id.total_scan_edittext);

            scan_count.setText(Integer.toString(currentAndroidFlavor.getScan_count()));
            return listItemView;
        }

    }
