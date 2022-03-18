package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PreviousJobsDetailsActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    RecyclerView recyclerView;
    AttendanceAdapterClass attendanceAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
      FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//    String userID = currentUser.getPhoneNumber();
    List<AttendanceModel> att_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_jobs_details);

        //   JobModel model = (JobModel)getIntent().getSerializableExtra("model");
        loadingDialog = new LoadingDialog(PreviousJobsDetailsActivity.this);
        Bundle bundle = getIntent().getExtras();
        String jobId = bundle.getString("JobId");
        String companyName = bundle.getString("CompanyName");


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
                        String jobName = (String) job.get("jobType");
                        String jobDesc = (String) job.get("description");
                        String jobLocation = (String) job.get("location");
                        String jobWage = (String) job.get("wage");
                        String jobId = (String) document.getId();
                        String companyId = (String) job.get("companyId");
                        String startdate = (String) job.get("startDate");
                        String enddate = (String) job.get("endDate");
                        String startime = (String) job.get("startTime");
                        String endtime = (String) job.get("endTime");
                        String required_workers = (String) job.get("required_workers");
                        String shortage = (String) job.get("vacancy");
                        String contact = (String) job.get("contact");


                        jobDetails model = new jobDetails(jobName, jobDesc, jobLocation, R.drawable.jobimage,jobWage,jobId,
                                companyId,companyName,startdate, enddate, startime, endtime, required_workers,
                                shortage, contact);


                        setDisplay(model);
                    } else {
                        System.out.println(task.getException());
                    }
                } else {
                    System.out.println("Error");
                }

            }
        });


        db.collection("att_test")
                .whereEqualTo("job_id", "1").whereEqualTo("user_id","1")
                .orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("DOCS: "+document.getData());
                                Map<String, Object> job=  document.getData();
                                String att_date = (String) job.get("date");
                                String check_in_time = (String) job.get("checkin_time");
                                String check_out_time = (String) job.get("checkout_time");
                                String on_time = (String) job.get("on_time");

                                att_list.add(new AttendanceModel(att_date,check_in_time,check_out_time,on_time));
                            }
                        } else {
                            System.out.println( "Error getting documents: "+ task.getException());
                        }
                        System.out.println("Completed");
                        setAttendace(att_list);
                        loadingDialog.dismissDialog();
                    }
                });



    }

    private void setAttendace(List<AttendanceModel> att_list) {
        recyclerView = findViewById(R.id.attendance_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceAdapter = new AttendanceAdapterClass(this, att_list);
        recyclerView.setAdapter(attendanceAdapter);
    }


    public void setDisplay(jobDetails model){
        TextView jobName = findViewById(R.id.details_jobType);
        TextView jobDesc = findViewById(R.id.details_jobDesc);
        TextView companyName = findViewById(R.id.details_companyName);
        TextView dailyWage = findViewById(R.id.details_dailyWage);
        TextView jobLocation = findViewById(R.id.details_Location);
        TextView startDate = findViewById(R.id.details_startDate);
        TextView endDate = findViewById(R.id.details_endDate);
        TextView startTime = findViewById(R.id.details_startTime);
        TextView endTime = findViewById(R.id.details_endTime);
        TextView contact = findViewById(R.id.details_jobContact);
//        RatingBar rating = findViewById(R.id.myjobs_details_Rating);
//        TextView payment_mode = findViewById(R.id.myjobs_details_jobPayment);


        jobName.setText(model.getJob_name());
        jobDesc.setText(model.getJob_desc());
        jobLocation.setText(model.getJob_location());
        companyName.setText(model.getCompanyname());
        startDate.setText(model.getStartdate());
        endDate.setText(model.getEnddate());
        dailyWage.setText(model.getWage());
        startTime.setText(model.getStartime());
        endTime.setText(model.getEndtime());
        contact.setText(model.getContact());

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