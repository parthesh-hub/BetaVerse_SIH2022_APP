package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.view.View;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    private TextView register;
    EditText editTextPhone2,editTextNumber3;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String verificationId,Code;
    ConstraintLayout loginotp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_login_page);
        Button login,verifyotp ;
        login = findViewById(R.id.loginGetOTP);
        editTextPhone2 = findViewById(R.id.editTextPhone2);
        loginotp = findViewById(R.id.constraintLayout);
        verifyotp = findViewById(R.id.loginVerifyOTP);
        editTextNumber3 = findViewById(R.id.editTextNumber3);
        mAuth = FirebaseAuth.getInstance();


        //Login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginotp.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("user").document("+91"+editTextPhone2.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            System.out.println("Document: "+document);
                            if (document.exists()) {
//                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                            startActivity(new Intent(LoginPage.this, Logout.class));
                                sendVerificationCode(editTextPhone2.getText().toString());
                            } else {
//                                        System.out.println("Document Snapshot data "+ document.getData());
                               Toast.makeText(LoginPage.this, "User Does Not Exist",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
            }
        });

        //VerifyOTP
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextNumber3.getText().toString()))
                {
                    Toast.makeText(LoginPage.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                }
                else
                    verifyCode(editTextNumber3.getText().toString());
            }

        });

        //Redirect to Register
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,FindJobs.class);
                startActivity(intent);
            }
        });
    }
// getOTP
private void sendVerificationCode(String phoneNumber) {
        System.out.println("inside sendveri");
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
            Toast.makeText(LoginPage.this, "Verification Failed", Toast.LENGTH_SHORT).show();
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
      PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,Code);
        System.out.println("Verify phone : "+verificationId);
        System.out.println("Code : "+Code);
        System.out.println("OTP entered : "+editTextNumber3.getText().toString());

        if(Code.equals(editTextNumber3.getText().toString())){
            Toast.makeText(LoginPage.this,"OTP Invalid",Toast.LENGTH_SHORT).show();
        }


        signinbyCredentials(credential);
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
                            Toast.makeText(LoginPage.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginPage.this, Logout.class));
                        }

                    }
                });

    }

}