package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_details);

        Button btn = findViewById(R.id.button);
     //   JobModel model = (JobModel)getIntent().getSerializableExtra("model");
        loadingDialog = new LoadingDialog(DetailsActivity.this);
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
//                                String companyName = companyId.companyName ;
                        String startdate = (String) job.get("startDate");
                        String enddate = (String) job.get("endDate");
                        String startime = (String) job.get("startTime");
                        String endtime = (String) job.get("endTime");
                        String required_workers = (String) job.get("required_workers");
                        String shortage = (String) job.get("vacancy");
                        String contact = (String) job.get("contact");

                        //getCompanyDetails(companyId);


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
                loadingDialog.dismissDialog();
            }
        });


    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DetailsActivity.this,Generate_QR.class);
            intent.putExtra("UserName","Sanket");
            intent.putExtra("UpiId","sanket@upi");
            intent.putExtra("ImageId",jobId);
            startActivity(intent);
        }
    });

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
        TextView availability = findViewById(R.id.details_availabiltiy);
        TextView contact = findViewById(R.id.details_jobContact);


        jobName.setText(model.getJob_name());
        jobDesc.setText(model.getJob_desc());
        jobLocation.setText(model.getJob_location());
        companyName.setText(model.getCompanyname());
        startDate.setText(model.getStartdate());
        endDate.setText(model.getEnddate());
        dailyWage.setText(model.getWage());
        startTime.setText(model.getStartdate());
        endTime.setText(model.getEnddate());
        availability.setText(model.getShortage());
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



