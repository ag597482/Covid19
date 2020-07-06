package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerate extends AppCompatActivity {


    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;
    String constant="Indra_co";
    String input_location;
    String text_to_qr="Indra";

    private ImageView imageView;
    private EditText location_text;
    int width=200,hight=200;
    
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");


        dataref= FirebaseDatabase.getInstance().getReference().child("users");
        setContentView(R.layout.activity_qr_code_generate);
        imageView=findViewById(R.id.qr_image);

        button=findViewById(R.id.generate_qr_button);
        location_text=findViewById(R.id.location_editview);
        String old_location_slected = getIntent().getData().toString();


        if(old_location_slected.isEmpty())
        {}
        else
        {

            text_to_qr = user_id+"#"+old_location_slected+"#Indra";
            QRCodeButton_Intent();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_location=location_text.getText().toString();
                if(input_location.isEmpty())
                {
                    Toast.makeText(QrCodeGenerate.this, "You Must fill the location name", Toast.LENGTH_SHORT).show();
                }
                else if(input_location.contains("."))
                {
                    Toast.makeText(QrCodeGenerate.this, "location name can NOT have Dot", Toast.LENGTH_SHORT).show();
                }
                else {
                    button.setVisibility(View.GONE);
                    location_text.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    QRCodeButton(v);
                }
            }
        });
    }


    public void QRCodeButton(View view)
    {


        FirebaseDatabase.getInstance().getReference().child("globle").child("globle num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long num=dataSnapshot.getValue(long.class);
                FirebaseDatabase.getInstance().getReference().child("globle").child("globle num").setValue(num+1);
                QRCodeWriter qrCodeWriter=new QRCodeWriter();
                try {

                    text_to_qr = user_id+"#"+input_location+"#Indra";
                    BitMatrix bitMatrix=qrCodeWriter.encode(text_to_qr, BarcodeFormat.QR_CODE,width,hight);
                    Bitmap bitmap=Bitmap.createBitmap(width,hight,Bitmap.Config.RGB_565);
                    for (int x = 0; x<200; x++){
                        for (int y=0; y<200; y++){
                            bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                        }
                    }
                    imageView.setImageBitmap(bitmap);

                    ScanGenFireData qrdata=new ScanGenFireData();
                    qrdata.setLocation_name(input_location);
                    qrdata.setQr_num(num);
                    dataref.child(user_id).child("qr generated").child(text_to_qr).setValue(qrdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void QRCodeButton_Intent()
    {
        button.setVisibility(View.GONE);
        location_text.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        try {
            BitMatrix bitMatrix=qrCodeWriter.encode(text_to_qr, BarcodeFormat.QR_CODE,width,hight);
            Bitmap bitmap=Bitmap.createBitmap(width,hight,Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}