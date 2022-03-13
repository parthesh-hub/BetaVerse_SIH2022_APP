package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TutorialVideoList extends AppCompatActivity {

    public static List<CategoryModel> catList = new ArrayList<>();
    public static int selected_cat_index = 0;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_tutorial_video_list);

        firestore = FirebaseFirestore.getInstance();


        Button showtutorial = (Button)findViewById(R.id.showtutorial);
        showtutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorialVideoList.this, VideoPlayer.class));
            }
        });

        Button showquiz = (Button)findViewById(R.id.showquiz);
        showquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(TutorialVideoList.this, QuizCategoryActivity.class));
                loadData();
            }
        });
    }

    private void loadData()
    {
        catList.clear();

        firestore.collection("Quiz").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i));
                            String catID = "CAT" + String.valueOf(i);

                            catList.add(new CategoryModel(catID,catName));
                        }


                        Intent intent = new Intent(TutorialVideoList.this, QuizCategoryActivity.class);
                        startActivity(intent);
                        TutorialVideoList.this.finish();

                    }
                    else
                    {
                        Toast.makeText(TutorialVideoList.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(TutorialVideoList.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}