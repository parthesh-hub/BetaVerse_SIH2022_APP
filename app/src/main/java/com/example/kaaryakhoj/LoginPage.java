package com.example.kaaryakhoj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.os.Handler;

public class LoginPage extends AppCompatActivity {

    private Button gotoRegisterbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        gotoRegisterbtn = findViewById(R.id.gotoRegister);
        gotoRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,VerifyPhone.class);
                startActivity(intent);
            }
        });
    }
}