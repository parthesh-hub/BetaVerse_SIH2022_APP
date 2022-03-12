package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindJobs extends AppCompatActivity {

    private RecyclerView jobRV;
    FirebaseFirestore db;
    private  JobAdapter adapter;
    // Arraylist for storing data
    private ArrayList<jobDetails> jobArrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.skills_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item.getTitle());
        filter((String) item.getTitle());
        return super.onOptionsItemSelected(item);
    }

    private void filter(String title) {
        ArrayList<jobDetails> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (jobDetails item : jobArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            //System.out.println(item.getJob_name());
            if (item.getJob_name().toLowerCase().contains(title.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            System.out.println("Filtered list "+filteredlist.get(0));

            setJobArray(filteredlist);
            System.out.println("Hello");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_jobs);
        jobRV = findViewById(R.id.findJobs);
        buildRecyclerView();




    }


    private void buildRecyclerView(){
        db = FirebaseFirestore.getInstance();
        // here we have created new array list and added data to it.
        jobArrayList = new ArrayList<>();
        db.collection("jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData());
                                Map<String, Object> job=  document.getData();
                                System.out.println("Json Object "+job);
                                String jobName = (String) job.get("jobName");
                                String jobLocation = (String) job.get("jobLocation");
                                String jobId = (String) document.getId();
                                System.out.println(("JOB NAME "+ jobName));
                                jobArrayList.add(new jobDetails(jobName,"", jobLocation, R.drawable.jobimage,"",jobId));
                                System.out.println(("JOb Array"+ jobArrayList));
                            }
                        } else {
                            System.out.println("Error getting documents: "+task.getException());
                        }
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setJobArray(jobArrayList);
//
//                            }
//                        }, 2000);
                        setJobArray(jobArrayList);

                    }
                });
    }
    private void setJobArray(ArrayList<jobDetails> jobList) {
        JobAdapter courseAdapter = new JobAdapter(this, jobList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        jobRV.setLayoutManager(linearLayoutManager);
        jobRV.setAdapter(courseAdapter);
    }
}