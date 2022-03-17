package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

public class FirstPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MyJobsFragment myJobsFragment = new MyJobsFragment();
    FindJobsFragment findJobsFragment = new FindJobsFragment();
    TutorialsFragment tutorialsFragment = new TutorialsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        getSupportActionBar().show();
        loadLocale();
        setContentView(R.layout.activity_first_page);

//
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#CE1C34"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myJobsFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_myjobs:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myJobsFragment).commit();
                    return true;
                case R.id.nav_findjobs:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, findJobsFragment).commit();
                    return true;
                case R.id.nav_tutorials:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tutorialsFragment).commit();
                    return true;
            }
            return true;
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