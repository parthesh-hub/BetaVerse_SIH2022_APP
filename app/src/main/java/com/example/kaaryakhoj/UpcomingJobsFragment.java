package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class UpcomingJobsFragment extends Fragment {


    View layoutView;
    Context thiscontext;
    LoadingDialog loadingDialog;
    private RecyclerView jobRV;
    FirebaseFirestore db;
    private  MyJobsAdapter adapter;
    // Arraylist for storing data
    private ArrayList<jobDetails> jobArrayList;
    private ArrayList<String> jobIds;
    String companyName;
    FirebaseUser currentUser;
    Date date1,enddate1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadLocale();
        thiscontext = container.getContext();
        setHasOptionsMenu(true);

        loadingDialog = new LoadingDialog(getActivity());
        layoutView = inflater.inflate(R.layout.fragment_upcoming_jobs, container, false);
        jobRV = (RecyclerView)layoutView.findViewById(R.id.upcomingJobs);
        buildRecyclerView();

        return layoutView;
    }




    private void buildRecyclerView(){
        db = FirebaseFirestore.getInstance();
        // here we have created new array list and added data to it.
        jobArrayList = new ArrayList<>();
        loadingDialog.startLoadingDialog();
        System.out.println(currentUser.getPhoneNumber());
        db.collection("jobRecords")
                .whereEqualTo("UserId",currentUser.getPhoneNumber())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().isEmpty()){
                                Toast.makeText(getContext(),"You have no upcoming Jobs",Toast.LENGTH_SHORT).show();
                            }else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> jobr = document.getData();
//                                    String job
                                    String jobid  =(String) jobr.get("JobId");
                                    System.out.println("JOB" + jobid);

                                    DocumentReference docRef = db.collection("jobs").document(jobid);
                                    System.out.println("doc: "+docRef);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                                    System.out.println("Inside doc: "+docRef);
                                                    if (task1.isSuccessful()) {
                                                        DocumentSnapshot document = task1.getResult();
                                                        System.out.println("doc: "+document);
                                                        if (document.exists()) {
                                                            System.out.println(document.getData());
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
                                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                            LocalDateTime now = LocalDateTime.now();
                                                            try {
                                                                date1 =new SimpleDateFormat("yyyy-MM-dd").parse( dtf.format(now));
                                                                enddate1 = new SimpleDateFormat("yyyy-MM-dd").parse( enddate);

                                                            } catch ( ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            String status = "";
                                                            if(jobr.get("status")!=null)
                                                            {
                                                                status = (String) jobr.get("status");
                                                            }
                                                            if((date1.before(enddate1) || date1.equals(enddate1)) && status.equals("ongoing")) {
                                                                jobArrayList.add(new jobDetails(jobName, jobDesc, jobLocation, R.drawable.jobimage, jobWage, jobId,
                                                                        companyId, "JPMC", startdate, enddate, startime, endtime, required_workers,
                                                                        shortage, contact,""));
                                                            }
                                                            System.out.println(("Job Array"+ jobArrayList));
                                                            System.out.println("JOBLIST: "+jobArrayList);

                                                            }else{
                                                            System.out.println("HEYY");

                                                        }
                                                }
                                            }



                                            });

                                }


                            }

                        }else{
                                    System.out.println("Error");
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                //startActivity(new Intent(MainActivity.this, LoginPage.class));

                                setJobArray(jobArrayList);
                                loadingDialog.dismissDialog();


                            }
                        }, 2000);

                    }

                });
//        db.collection("jobs")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                System.out.println(document.getData());
//                                Map<String, Object> job=  document.getData();
//                                System.out.println("Json Object "+job);
//                                String jobName = (String) job.get("jobType");
//                                String jobDesc = (String) job.get("description");
//                                String jobLocation = (String) job.get("location");
//                                String jobWage = (String) job.get("wage");
//                                String jobId = (String) document.getId();
//                                String companyId = (String) job.get("companyId");
//
////                                String companyName = companyId.companyName ;
//                                String startdate = (String) job.get("startDate");
//                                String enddate = (String) job.get("endDate");
//                                String startime = (String) job.get("startTime");
//                                String endtime = (String) job.get("endTime");
//                                String required_workers = (String) job.get("required_workers");
//                                String shortage = (String) job.get("vacancy");
//                                String contact = (String) job.get("contact");
//
//                                jobArrayList.add(new jobDetails(jobName, jobDesc, jobLocation, R.drawable.jobimage,jobWage,jobId,
//                                        companyId,"JPMC",startdate, enddate, startime, endtime, required_workers,
//                                        shortage, contact));
//
//                                System.out.println(("Job Array"+ jobArrayList));
//                            }
//                        } else {
//                            System.out.println("Error getting documents: "+task.getException());
//                        }
//                        setJobArray(jobArrayList);
//                        loadingDialog.dismissDialog();
//                    }
//                });
    }


    private void setJobArray(ArrayList<jobDetails> jobList) {
        MyJobsAdapter courseAdapter = new MyJobsAdapter(getActivity(), jobList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        System.out.println("JOBRV: "+jobRV);
        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        jobRV.setLayoutManager(linearLayoutManager);
        jobRV.setAdapter(courseAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
//        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.skills_menu,menu);

        super.onCreateOptionsMenu(menu, menuInflater);
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
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            System.out.println("Filtered list "+filteredlist.get(0));

            setJobArray(filteredlist);
            System.out.println("Hello");
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

        //save data to shared prefernces
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language stored in Shared Preferences
    public void loadLocale(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

}