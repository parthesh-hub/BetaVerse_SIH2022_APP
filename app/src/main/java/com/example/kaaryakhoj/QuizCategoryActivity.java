package com.example.kaaryakhoj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import static com.example.kaaryakhoj.TutorialsFragment.catList;

public class QuizCategoryActivity extends AppCompatActivity {

    private GridView catGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category);


        System.out.println("yes");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Categories");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catGrid = findViewById(R.id.catGridview);

        System.out.println("yes2");
        CatGridAdapter adapter = new CatGridAdapter(catList);
        catGrid.setAdapter(adapter);
        System.out.println("yes3");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            QuizCategoryActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


