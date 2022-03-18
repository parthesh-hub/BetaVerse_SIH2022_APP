package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyJobsDetailsActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    RecyclerView recyclerView;
    AttendanceAdapterClass attendanceAdapter;

    String jobId1,startdate,enddate,userId,jobWage;
    Date startdate1,date1,enddate1,date2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    //FirebaseFirestore db1 = FirebaseFirestore.getInstance();

    List<String> dateArray
            = new ArrayList<String>();
    Object[] arr = new String[100];
    int flag=0;
    //    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//    String userID = currentUser.getPhoneNumber();
    List<AttendanceModel> att_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_my_jobs_upcoming_details);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Button btn = findViewById(R.id.myjobs_generateQR);
        //   JobModel model = (JobModel)getIntent().getSerializableExtra("model");
        loadingDialog = new LoadingDialog(MyJobsDetailsActivity.this);
        Bundle bundle = getIntent().getExtras();
        String jobId = bundle.getString("JobId");
        String companyName ="";
        Button cancel = findViewById(R.id.myjobs_cancel);

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
                         jobWage = (String) job.get("wage");
                        String jobId = (String) document.getId();
                        String companyId = (String) job.get("companyId");
                         startdate = (String) job.get("startDate");
                         enddate = (String) job.get("endDate");
                        String startime = (String) job.get("startTime");
                        String endtime = (String) job.get("endTime");
                        String required_workers = (String) job.get("required_workers");
                        String shortage = (String) job.get("vacancy");
                        String contact = (String) job.get("contact");

                        jobId1 = jobId;
                        try {
                            startdate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startdate);
                            enddate1 = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

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
//                        setAttendace(att_list);
                        loadingDialog.dismissDialog();
                    }
                });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyJobsDetailsActivity.this,Generate_QR.class);
                intent.putExtra("UserId",currentUser.getPhoneNumber());
                intent.putExtra("JobId",jobId1);
                intent.putExtra("StartDate",startdate);
                intent.putExtra("EndDate",enddate);
                intent.putExtra("Amount",jobWage);
                intent.putExtra("AccountId","account@upi");
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userId = "+919158346466";
                db.collection("jobRecords")
                        .whereEqualTo("JobId", jobId1)
                        .whereEqualTo("UserId",userId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                        System.out.println("Inside Document");
                                        String jobRecordId = (String) document.getId();
                                        db.collection("jobRecords").document(jobRecordId)
                                                .update("status","cancelled")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        System.out.println("Success");
                                                        cancel(userId,enddate);
//                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error updating document", e);
                                                        System.out.println(e);
                                                    }
                                                });
                                    }
                                } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });


    }

    private void cancel(String userId,String endDate) {
        System.out.println("Inside User");
        DocumentReference docRef = db.collection("user").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> job=  document.getData();
                        List<String > date = new  ArrayList<String>();

                        date = (List<String>) job.get("enddate");
                        date.remove(endDate);

                        db.collection("user").document(userId).update("enddate", date)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("Success");

//                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
//                        Log.d(TAG, "No such document");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
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