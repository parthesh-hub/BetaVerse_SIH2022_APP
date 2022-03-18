package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    private TextView register, chnglang;
    EditText entered_phone,entered_otp;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String verificationId,Code;
    ConstraintLayout loginotp;
    Button getOtpbtn, loginbtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        loadLocale();
        setContentView(R.layout.activity_login_page);

        getOtpbtn = findViewById(R.id.loginpage_getOtpbtn);
        entered_phone = findViewById(R.id.loginpage_phone);
        loginbtn = findViewById(R.id.loginpage_loginbtn);
        entered_otp = findViewById(R.id.loginpage_enterotp);

        mAuth = FirebaseAuth.getInstance();


        //Login Button
        getOtpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getOtpbtn.setEnabled(false);
                db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("user").document("+91"+entered_phone.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            System.out.println("Document: "+document);
                            if (document.exists()) {
//                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                            startActivity(new Intent(LoginPage.this, Logout.class));
                                sendVerificationCode(entered_phone.getText().toString());
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
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(entered_otp.getText().toString()))
                {
                    Toast.makeText(LoginPage.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                }
                else
                    verifyCode(entered_otp.getText().toString());
            }

        });

        //Redirect to Register
        register = findViewById(R.id.loginpage_registerhere);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,FirstPage.class);
                startActivity(intent);
            }
        });


        // language translator
        chnglang = findViewById(R.id.loginpage_changelanguage);
        chnglang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });


    }


    // language translation
    private void showChangeLanguageDialog() {

        //array of languages to display in dialogbox
        final String[] listItems = {"English", "हिन्दी", "मराठी", "ગુજરાતી", "தமிழ்", "తెలుగు"};

        AlertDialog.Builder mBuider = new AlertDialog.Builder(LoginPage.this);
        mBuider.setTitle("Choose Language...");
        mBuider.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i==0){
                    //English
                    setLocale("en");
                    recreate();
                }
                else if (i==1){
                    //hindi
                    setLocale("hi");
                    recreate();
                }
                else if (i==2) {
                    //marathi
                    setLocale("mr");
                    recreate();
                }
                else if (i==3) {
                    //gujarati
                    setLocale("gu");
                    recreate();
                }
                else if (i==4) {
                    //tamil
                    setLocale("ta");
                    recreate();
                }
                else if (i==5) {
                    //telugu
                    setLocale("te");
                    recreate();
                }

                //dismiss alert dialog box when language is selected
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuider.create();
        //show alert dialog
        mDialog.show();

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
        System.out.println("OTP entered : "+entered_otp.getText().toString());

        if (!(Code.equals(entered_otp.getText().toString()))){
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
                            startActivity(new Intent(LoginPage.this, FirstPage.class));
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
            startActivity(new Intent(LoginPage.this, FirstPage.class));
            finish();
        }


    }





}