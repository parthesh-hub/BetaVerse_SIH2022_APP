package com.example.kaaryakhoj;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.TextUtils;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class VerifyPhone extends AppCompatActivity {
    private Button button3;
    private EditText editTextPhone,otp;
    private Button button,otpverify;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ConstraintLayout verifyotplayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_verify_phone);

//        button3 = findViewById(R.id.otpverify);
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VerifyPhone.this,RegistrationPage.class);
//                startActivity(intent);
//            }
//        });

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        button = (Button) findViewById(R.id.button);
        otp = (EditText) findViewById(R.id.otp);
        otpverify = (Button) findViewById(R.id.otpverify);
        mAuth = FirebaseAuth.getInstance();
        System.out.println("mAuth inside verification page"+ mAuth);
        verifyotplayout = (ConstraintLayout) findViewById(R.id.verifyotpview);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextPhone.getText().toString()))
                {
                    Toast.makeText(VerifyPhone.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String number = editTextPhone.getText().toString();
                    verifyotplayout.setVisibility(View.VISIBLE);
//                    bar.setVisibility(View.VISIBLE);
                    sendVerificationCode(number);

                }


            }
        });

        otpverify.setOnClickListener(new View.OnClickListener() {
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

        Intent intent = new Intent(VerifyPhone.this, RegistrationPage.class);
//        intent.putExtra("credential", credential);
        intent.putExtra("verificationId", verificationId);
        intent.putExtra("Code", Code);
        startActivity(intent);

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println("Hello");
        System.out.println(currentUser);

        if(currentUser!=null)
        {
            startActivity(new Intent(VerifyPhone.this, Logout.class));
            finish();
        }


    }





    }
