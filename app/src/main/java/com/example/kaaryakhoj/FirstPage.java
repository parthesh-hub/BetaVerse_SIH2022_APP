package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
        setContentView(R.layout.activity_first_page);

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
}