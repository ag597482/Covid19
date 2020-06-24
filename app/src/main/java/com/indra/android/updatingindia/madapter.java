package com.indra.android.updatingindia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class  madapter extends RecyclerView.Adapter<madapter.madapterholser>  {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<medi_info_card> list;

    //getting the context and product list with constructor
    public madapter(Context mCtx, List<medi_info_card> jewelryList) {
        this.mCtx = mCtx;
        this.list  = jewelryList;
    }


    @NonNull
    @Override
    public madapter.madapterholser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.medi_list_item, null);
        return new madapterholser(view);

    }



    @Override
    public void onBindViewHolder(@NonNull madapterholser holder, int position) {

        medi_info_card medi=list.get(position);

        holder.t1.setText(medi.getHospital_name());
        holder.t2.setText(medi.getBed());
        holder.t3.setText(medi.getCity());


    }





    @Override
    public int getItemCount() {
        return list.size();
    }


    public class madapterholser extends RecyclerView.ViewHolder {

        TextView t1,t2,t3;
        public madapterholser(@NonNull View itemView) {
            super(itemView);


            t1 = itemView.findViewById(R.id.hospital_name);
            t2 = itemView.findViewById(R.id.bed_count);
            t3 = itemView.findViewById(R.id.city_name);
        }
    }
}
