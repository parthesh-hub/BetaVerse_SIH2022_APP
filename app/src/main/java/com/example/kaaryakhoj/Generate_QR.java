package com.example.kaaryakhoj;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Generate_QR extends AppCompatActivity {

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private EditText dataEdt;
    private Button downloadQrBtn;
    Bitmap mMap;
    String jobId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_generate_qr);

        String UserName = getIntent().getStringExtra("UserName");
        String UpiId = getIntent().getStringExtra("UpiId");
        System.out.println("Username "+UserName);
        downloadQrBtn = findViewById(R.id.idDownloadQR);
        qrCodeIV = findViewById(R.id.idIVQrcode);
        jobId = getIntent().getStringExtra("ImageId");

        MultiFormatWriter mWriter = new MultiFormatWriter();

        try{
            JSONObject obj = new JSONObject();

            obj.put("UserName",UserName);
            obj.put("UpiId",UpiId);

//                    StringWriter out = new StringWriter();
//                    obj.write(out);

            String text = obj.toString();
            System.out.print(text);
            BitMatrix mMatrix = mWriter.encode(text, BarcodeFormat.QR_CODE,400,400);

            BarcodeEncoder mEncoder = new BarcodeEncoder();
            mMap = mEncoder.createBitmap(mMatrix);
            qrCodeIV.setImageBitmap(mMap);

            // InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        
            

        }catch (WriterException | JSONException e){
            e.printStackTrace();
        }
        
        downloadQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaStore.Images.Media.insertImage(getContentResolver(), mMap, jobId , "SAVE QR");
            }
        });
    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared prefernces
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language stored in Shared Preferences
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

}