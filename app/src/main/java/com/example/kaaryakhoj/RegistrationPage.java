package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;


import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    private EditText Name,Address,aadharNumber;
    private Button button2;
    private FirebaseFirestore db;
    private PhoneAuthCredential credential;
    private String userID, Code, verificationId;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        Name = findViewById(R.id.Name);
        Address = findViewById(R.id.Address);
        aadharNumber = findViewById(R.id.aadharNumber);
        button2 = findViewById(R.id.button2);

//        credential = getIntent().getParcelableExtra("credential");
        verificationId = getIntent().getStringExtra("verificationId");
        Code = getIntent().getStringExtra("Code");
        System.out.println("Registration Page Hello"+verificationId + Code);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,Code);
                System.out.println("Credential: "+credential);
                System.out.println("Inside click");
                signinbyCredentials(credential);
                System.out.println("After signin");

                userID = mAuth.getCurrentUser().getUid();

                System.out.println("After userID");
                System.out.println("Registration Page UserID"+userID);
                String name = Name.getText().toString();
                System.out.println("Registration Page Name"+name);
                String address = Name.getText().toString();
                String aadhar = aadharNumber.getText().toString();
                Map<String,Object> user =new HashMap<>();
                user.put("Name", name);
                user.put("Address", address);
                user.put("Aadhar Number", aadhar);


                db.collection("user").document(userID)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegistrationPage.this,"Success",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationPage.this,"Failure",Toast.LENGTH_SHORT).show();
                            }
                        });


//                db.collection("user")
//                        .add(user)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(RegistrationPage.this,"Success",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(RegistrationPage.this,"Failure",Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });


    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
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
                            startActivity(new Intent(RegistrationPage.this, Logout.class));
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