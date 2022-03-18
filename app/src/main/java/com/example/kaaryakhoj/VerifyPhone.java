package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class VerifyPhone extends AppCompatActivity {
    private Button button3;
    private EditText phonenumber,otp;
    Button getotpbutton,otpverifybtn;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ConstraintLayout verifyotplayout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        loadLocale();
        setContentView(R.layout.activity_verify_phone);


        phonenumber = (EditText) findViewById(R.id.verifyphonepage_phone);
        getotpbutton = (Button) findViewById(R.id.verifyphonepage_getOtpbtn);
        otp = (EditText) findViewById(R.id.verifyphonepage_enterotp);
        otpverifybtn = (Button) findViewById(R.id.verifyphonepage_verifyotpbtn);
        mAuth = FirebaseAuth.getInstance();
        System.out.println("mAuth inside verification page"+ mAuth);
        verifyotplayout = (ConstraintLayout) findViewById(R.id.constraintLayout2);


        getotpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phonenumber.getText().toString()))
                {
                    Toast.makeText(VerifyPhone.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String number = phonenumber.getText().toString();
                    verifyotplayout.setVisibility(View.VISIBLE);
                    getotpbutton.setEnabled(false);
//                    bar.setVisibility(View.VISIBLE);
                    db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("user").document("+91"+phonenumber.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                System.out.println("Document: "+document);
                                if (document.exists()) {
//                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    System.out.println("Document Snapshot data "+ document.getData());
                                    Toast.makeText(VerifyPhone.this, "User Already Exists",Toast.LENGTH_SHORT).show();
                                } else {
//                                        Log.d(TAG, "No such document");
                                    sendVerificationCode(number);
                                }
                            }
                        }
                    });
//                    sendVerificationCode(number);

                }


            }
        });

        otpverifybtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString()))
                {
                    Toast.makeText(VerifyPhone.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                }
                else
                    verifyCode(otp.getText().toString());
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


    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
            final String code = credential.getSmsCode();
            if (code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            super.onCodeSent(s,token);


            // Save verification ID and resending token so we can use them later
            verificationId = s;

        }
    };

    private void verifyCode(String Code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,Code);
        System.out.println("Verify phone : "+verificationId+Code);
        System.out.print("OTP: "+otp.getText().toString());
        if(!(Code.equals(otp.getText().toString()))){
            Toast.makeText(VerifyPhone.this,"OTP Invalid",Toast.LENGTH_SHORT).show();
        }
        else {

            Intent intent = new Intent(VerifyPhone.this, RegistrationPage.class);
//        intent.putExtra("credential", credential);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("Code", Code);
            startActivity(intent);
        }
//        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(VerifyPhone.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VerifyPhone.this, Logout.class));
                        }

                    }
                });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        System.out.println("Hello");
//        System.out.println(currentUser);
//
//        if(currentUser!=null)
//        {
//            startActivity(new Intent(VerifyPhone.this, Logout.class));
//            finish();
//        }
//
//
//    }





    }
