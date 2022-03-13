package com.example.kaaryakhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyJobsDetailsActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    RecyclerView recyclerView;
    AttendanceAdapterClass attendanceAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs_details);


        Button btn = findViewById(R.id.myjobs_generateQR);
        //   JobModel model = (JobModel)getIntent().getSerializableExtra("model");
        loadingDialog = new LoadingDialog(MyJobsDetailsActivity.this);
        Bundle bundle = getIntent().getExtras();
        String jobId = bundle.getString("JobId");

        DocumentReference docRef = db.collection("jobs").document(jobId);

        loadingDialog.startLoadingDialog();
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
                loadingDialog.dismissDialog();
            }
        });


        recyclerView = findViewById(R.id.attendance_recycler_view);

        setRecyclerView();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyJobsDetailsActivity.this,Generate_QR.class);
                intent.putExtra("UserName","Sanket");
                intent.putExtra("UpiId","sanket@upi");
                intent.putExtra("ImageId",jobId);
                startActivity(intent);
            }
        });

    }

    private void setRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceAdapter = new AttendanceAdapterClass(this, getList());
        recyclerView.setAdapter(attendanceAdapter);

    }

    private List<AttendanceModel> getList() {
        List<AttendanceModel> att_list = new ArrayList<>();
        att_list.add(new AttendanceModel("01-02-2022","09:00","17:00","OnTime"));
        att_list.add(new AttendanceModel("02-02-2022","09:10","17:00","OnTime"));
        att_list.add(new AttendanceModel("03-02-2022","09:30","17:30","Late"));
        att_list.add(new AttendanceModel("04-02-2022","10:00","18:00","Late"));

        return att_list;
    }


    public void setDisplay(jobDetails model){
        TextView jobName = findViewById(R.id.myjobs_details_strjobType);
        TextView jobLocation = findViewById(R.id.myjobs_details_Location);
        TextView jobDesc = findViewById(R.id.myjobs_details_jobDesc);
        TextView wage = findViewById(R.id.myjobs_details_dailyWage);

        jobName.setText(model.getJob_name());
        jobLocation.setText(model.getJob_location()+"");
        jobDesc.setText(model.getJob_desc()+"");
        wage.setText(model.getWage());
    }
}