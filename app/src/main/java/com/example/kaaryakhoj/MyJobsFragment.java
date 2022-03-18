package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;


public class MyJobsFragment extends Fragment {

    View layoutview;
    private TabLayout tabLayout;
    private ViewPager viewPager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadLocale();
        layoutview =  inflater.inflate(R.layout.fragment_myjobs, container, false);
        TabLayout tabLayout = (TabLayout) layoutview.findViewById(R.id.myjobstab);
        ViewPager viewPager = (ViewPager) layoutview.findViewById(R.id.myjobsviewpager);


        System.out.println("after call");

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new UpcomingJobsFragment(),"Upcoming Jobs");
        vpAdapter.addFragment(new PreviousJobsFragment(),"Previous Jobs");
        loadLocale();
        viewPager.setAdapter(vpAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                loadLocale();
            }
        });

        return layoutview;

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

        System.out.println("Inside SetLocale  ");
        //save data to shared prefernces
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language stored in Shared Preferences
    public void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        System.out.println("Lang: "+language);
        setLocale(language);
    }



}