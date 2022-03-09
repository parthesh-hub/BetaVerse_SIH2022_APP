package com.example.kaaryakhoj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main2);


        // LogOut Button
        btnlogout = findViewById(R.id.button3);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Logout.this, LoginPage.class));
                finish();
            }
        });
    }
}