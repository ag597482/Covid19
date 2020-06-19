package com.indra.android.covid19;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Policy And Additional Information")
                .setMessage("*All the information is Taken from Api https://api.rootnet.in/covid19-in/hospitals/medical-colleges.\n \n*Hospital included are ownership by goverment,Trust,Society and are not necesserly COVID hospitals." +
                        "\n \n*Count of Beds are " +
                        "not fetched live, The data may be old and fixed. \n  \n*We are trying to give rough idea about medical and hospital condition in a " +
                        "state. \n \n*Theses are the sources for all the data presented in the app source1 - https://api.covid19india.org/state_district_wise.json and source2 - https://api.covid19india.org/state_district_wise.json" +
                        "\n \n*Our motive - Through this application we have tried to update you with above api information easily." +
                        "\n \n*We are not responsible for any data to be found incorrect, as we are not involve in data making we are just helping the user to access the " +
                        "data from above mentioned sources(API) easily and if any one found any thing wrong feel free to inform us and also it verify from other sources")
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return builder.create();
    }
}