package com.indra.android.updatingindia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.StrictMath.min;

public class scan_history_recycle_view extends RecyclerView.Adapter<scan_history_recycle_view.myholder> {

    public ArrayList<scan_history_info> scan_history_infos_array;
    Context mcontext;

    public scan_history_recycle_view(Context mcontext,ArrayList<scan_history_info> scan_history_infos_array) {
        this.scan_history_infos_array = scan_history_infos_array;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate=LayoutInflater.from(parent.getContext());
        View view=inflate.inflate(R.layout.scan_card_list,parent,false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        final scan_history_info scan_card=scan_history_infos_array.get(position);
        Log.i("array addpater"," ------------------------------------- "+scan_history_infos_array.size()+"  "+scan_card.getLocation_name());
        holder.location_name.setText(scan_card.getLocation_name());
        holder.scan_count.setText(Long.toString(scan_card.getScan_count()));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,QrCodeGenerate.class);
                intent.setData(Uri.parse(scan_card.getQr_text()));
                Log.i("scan_holder_adapter","-------------------qr_text"+scan_card.getQr_text());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scan_history_infos_array.size();
    }

    public class myholder extends RecyclerView.ViewHolder {

        TextView location_name,scan_count;
        ConstraintLayout parent_layout;
        public myholder(@NonNull View itemView) {
            super(itemView);
            location_name= itemView.findViewById(R.id.location_name_scan);
            scan_count= itemView.findViewById(R.id.total_scan_edittext);
            parent_layout=itemView.findViewById(R.id.parent_layout);
        }
    }
}
