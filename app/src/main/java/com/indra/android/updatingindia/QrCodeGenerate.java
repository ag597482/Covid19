package com.indra.android.updatingindia;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeGenerate extends AppCompatActivity {

    String text_to_qr="Indra_co";
    private ImageView imageView;
    private EditText location_text;
    int width=200,hight=200;
    
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generate);
        imageView=findViewById(R.id.qr_image);

        button=findViewById(R.id.generate_qr_button);
        location_text=findViewById(R.id.location_editview);
        String old_location_slected = getIntent().getData().toString();
        if(old_location_slected.isEmpty())
        {}
        else
        {
            text_to_qr=old_location_slected;
            QRCodeButton_Intent();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_to_qr=location_text.getText().toString();
                if(text_to_qr.isEmpty())
                {
                    Toast.makeText(QrCodeGenerate.this, "You Must fill the location name", Toast.LENGTH_SHORT).show();
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
            ScanHistory.scan_history_infos_array.add(new scan_history_info(location_text.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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