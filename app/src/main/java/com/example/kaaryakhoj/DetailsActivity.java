package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

     //   JobModel model = (JobModel)getIntent().getSerializableExtra("model");

        Bundle bundle = getIntent().getExtras();
        String jobId = bundle.getString("JobId");
        DocumentReference docRef = db.collection("jobs").document(jobId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> job=  document.getData();
                        System.out.println("Json Object "+job);
                        String jobName = (String) job.get("jobName");
                        String jobLocation = (String) job.get("jobLocation");
//                                String jobId = (String) document.getId();
                        String jobDesc = (String) job.get("jobDesc");
                        String wage = (String) job.get("wage");
                        System.out.println(("JOB NAME "+ jobName));
                        jobDetails model = new jobDetails(jobName,jobDesc,jobLocation,R.drawable.jobimage,wage,"");
                        // model(new jobDetails(jobName,"", jobLocation, R.drawable.jobimage,"",jobId));
                        //System.out.println(("JOb Array"+ model));
                        setDisplay(model);
                    } else {
                        System.out.println(task.getException());
                    }
                } else {
                    System.out.println("Error");
                }
            }
        });




    }
    public void setDisplay(jobDetails model){
        TextView jobName = findViewById(R.id.jobName);
        TextView jobLocation = findViewById(R.id.jobLocation);
        TextView jobDesc = findViewById(R.id.jobDesc);
        TextView wage = findViewById(R.id.wage);

        jobName.setText(model.getJob_name());
        jobLocation.setText(model.getJob_location()+"");
        jobDesc.setText(model.getJob_desc()+"");
       wage.setText(model.getWage());
    }
}