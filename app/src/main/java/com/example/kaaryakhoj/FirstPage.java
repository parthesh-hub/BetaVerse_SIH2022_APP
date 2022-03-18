package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class FirstPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MyJobsFragment myJobsFragment = new MyJobsFragment();
    FindJobsFragment findJobsFragment = new FindJobsFragment();
    TutorialsFragment tutorialsFragment = new TutorialsFragment();
    MyProfileFragment profileFragment = new MyProfileFragment();
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        getSupportActionBar().show();
        loadLocale();
        setContentView(R.layout.activity_first_page);
//        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

//
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#CE1C34"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

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
                    case R.id.nav_profilepage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
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