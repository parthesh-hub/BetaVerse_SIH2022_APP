package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RegistrationPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    private EditText FirstName, LastName, Pincode, Address,aadharNumber;
    private Button registerbtn;
    private FirebaseFirestore db;
    private PhoneAuthCredential credential;
    private String userID, Code, verificationId,Phone;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        loadingDialog = new LoadingDialog(RegistrationPage.this);
        loadLocale();
        setContentView(R.layout.activity_registration_page);

        db = FirebaseFirestore.getInstance();
        FirstName = findViewById(R.id.registrationpage_firstname);
        LastName = findViewById(R.id.registrationpage_lastname);
        Pincode = findViewById(R.id.registrationpage_pincode);
        Address = findViewById(R.id.registrationpage_PostalAddress);
        aadharNumber = findViewById(R.id.registrationpage_aadhar);
        registerbtn = findViewById(R.id.registrationpage_registerbtn);

        credential = getIntent().getParcelableExtra("credential");
        verificationId = getIntent().getStringExtra("verificationId");
        Code = getIntent().getStringExtra("Code");
        System.out.println("Registration Page Hello"+verificationId + Code);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,Code);
        System.out.println("Credential: "+credential);
        System.out.println("Inside click");

        System.out.println("After signin");

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,Code);
                System.out.println("Credential: "+credential);
                System.out.println("Inside click");

                loadingDialog.startLoadingDialog();
                signinbyCredentials(credential);

                System.out.println("After signin");
                signinbyCredentials(credential);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        Phone = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
                        System.out.println("After userID");
                        System.out.println("Registration Page UserID"+userID);
                        String firstname = FirstName.getText().toString();
                        String lastname = LastName.getText().toString();
                        String pincode = Pincode.getText().toString();
                        String address = Address.getText().toString();
                        String aadhar = aadharNumber.getText().toString();
                        Map<String,Object> user =new HashMap<>();
                        user.put("Firstname", firstname);
                        user.put("Lastname", lastname);
                        user.put("Address", address);
                        user.put("Pincode",pincode);
                        user.put("AadharNumber", aadhar);
                        user.put("Phone Number", Phone);
                        user.put("UpiId","worker@upi");
                        user.put("overall_rating","0");
                        user.put("experience","0");
                        user.put("Skills Rating","");


                        db.collection("user").document(Phone)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegistrationPage.this,"Success",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistrationPage.this,"Failure",Toast.LENGTH_SHORT).show();
                                    }
                                });


                        finish();
                    }
                }, 2000);
                loadingDialog.dismissDialog();
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


    public void signin(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                signinbyCredentials(credential);
                finish();
            }
        }, 4000);

    }

    public void signinbyCredentials(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth);
        System.out.println("Credential inside signin: "+credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        System.out.println("Inside onComplete");

                        if(task.isSuccessful())
                        {
                            System.out.println("Inside success");
                            Toast.makeText(RegistrationPage.this, "Login Successfull", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegistrationPage.this, Logout.class));
                        }
                        else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            System.out.println("Inside Else error " + task.getException().getMessage());

                            Toast.makeText(RegistrationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }
}