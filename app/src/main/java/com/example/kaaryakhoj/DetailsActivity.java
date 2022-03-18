package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    String jobId1,startdate,enddate,userId;
    Date startdate1,date1,enddate1,date2;
    List<String> dateArray
            = new ArrayList<String>();
    Object[] arr = new String[100];
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_details);

        Button btn = findViewById(R.id.details_applybtn);
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
                         startdate = (String) job.get("startDate");
                         enddate = (String) job.get("endDate");
                        String startime = (String) job.get("startTime");
                        String endtime = (String) job.get("endTime");
                        String required_workers = (String) job.get("required_workers");
                        String shortage = (String) job.get("vacancy");
                        String contact = (String) job.get("contact");
                        jobId1 = jobId;
                        //getCompanyDetails(companyId);
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
                loadingDialog.dismissDialog();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent intent = new Intent(DetailsActivity.this,Generate_QR.class);
//            intent.putExtra("UserName","Sanket");
//            intent.putExtra("UpiId","sanket@upi");
//            intent.putExtra("ImageId",jobId);
//            startActivity(intent);
                db = FirebaseFirestore.getInstance();
                System.out.println("Inside btn");
                userId = "+919158346466";
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
                                date  = (List<String>) job.get("enddate");
                                System.out.println("DATE: "+date);
                                //System.out.println("Date"+date[0]);
                                if(date!=null)
                                {
                                    if(date.isEmpty()){
                                        date = null;
                                    }
                                }
                                if(date==null)
                                {
                                    System.out.println(enddate);
                                    dateArray.add(enddate);
//                                arr =  dateArray.toArray();
                                    setJobRecord( dateArray);

                                }
                                else {
                                    try {
                                        date1 = new SimpleDateFormat("yyyy-MM-dd").parse((String) date.get((date.size()) - 1));


                                        if (startdate1.before(date1) || startdate1.equals(date1)) {
//                                            if(startdate1.equals(date1) || date.size()==1 )
//                                            {
//                                                System.out.println("Failed");
//                                                Toast.makeText(DetailsActivity.this, "Already in a Job", Toast.LENGTH_SHORT).show();
//
//                                            }else{
//                                                date2 = new SimpleDateFormat("yyyy-MM-dd").parse((String) date.get((date.size()) - 2));
//                                                if(startdate1.after(date2) && enddate1.before(date1)){
//                                                    date.add(enddate);
//
//                                                    setJobRecord(date);
//                                                }
                                            Toast.makeText(DetailsActivity.this, "Already in a Job", Toast.LENGTH_SHORT).show();
                                        }
//                                            if(startdate1.after(date1) || startdate1.equals(date1))
//                                            Toast.makeText(DetailsActivity.this, "Already in a Job", Toast.LENGTH_SHORT).show();
                                        else {
                                            date.add(enddate);

                                            setJobRecord(date);
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
//                        Log.d(TAG, "No such document");
                            }
                        } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


                //FirebaseFirestore firestore = FirebaseFirestore.getInstance();


            }


        });


    }

    private void setJobRecord(List<String> dateArray1) {

        System.out.println("Inside setJobs");
        Map<String, Object> city = new HashMap<>();
        city.put("JobId", jobId1);
        city.put("UserId", userId);
        city.put("StartDate", startdate);
        city.put("EndDate", enddate);

        db.collection("jobRecords")
                .add(city)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        db.collection("user").document(userId).update("enddate", dateArray1)
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
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
        TextView availability = findViewById(R.id.details_Availabiltiy);
        TextView contact = findViewById(R.id.details_jobContact);


        jobName.setText(model.getJob_name());
        jobDesc.setText(model.getJob_desc());
        jobLocation.setText(model.getJob_location());
        companyName.setText(model.getCompanyname());
        startDate.setText(model.getStartdate());
        endDate.setText(model.getEnddate());
        dailyWage.setText(model.getWage());
        startTime.setText(model.getStartime());
        endTime.setText(model.getEndtime());
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



