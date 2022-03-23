package com.example.kaaryakhoj;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TutorialsFragment extends Fragment {

    public static List<CategoryModel> catList = new ArrayList<>();
    public static int selected_cat_index = 0;
    private FirebaseFirestore firestore;
    View layoutview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        layoutview = inflater.inflate(R.layout.activity_tutorial_video_list, container, false);

        firestore = FirebaseFirestore.getInstance();


        Button showtutorial = (Button)layoutview.findViewById(R.id.showtutorial);
        showtutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VideoPlayer.class));
            }
        });

        Button showquiz = (Button)layoutview.findViewById(R.id.showquiz);
        showquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(TutorialVideoList.this, QuizCategoryActivity.class));
                loadData();
            }
        });

        return layoutview;
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
                            System.out.println(i);
                            catList.add(new CategoryModel(catID,catName));
                        }


                        Intent intent = new Intent(getActivity(), QuizCategoryActivity.class);
                        startActivity(intent);
//                        getActivity().finish();

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }

                }
                else
                {

                    Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}