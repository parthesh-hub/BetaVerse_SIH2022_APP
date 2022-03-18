package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;


public class FindJobsFragment extends Fragment {


    View layoutView;
    Context thiscontext;
    LoadingDialog loadingDialog;
    private RecyclerView jobRV;
    FirebaseFirestore db;
    private  JobAdapter adapter;
    // Arraylist for storing data
    private ArrayList<jobDetails> jobArrayList;
    private ArrayList<jobDetails> currentList;
    String companyName;
    String clickedMenu="",locItem="",jobItem="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thiscontext = container.getContext();
        loadingDialog = new LoadingDialog(getActivity());
        loadLocale();
        setHasOptionsMenu(true);
        layoutView = inflater.inflate(R.layout.activity_find_jobs, container, false);
        jobRV = (RecyclerView)layoutView.findViewById(R.id.findJobs);
        buildRecyclerView();

        return layoutView;
    }


    private void buildRecyclerView(){
        db = FirebaseFirestore.getInstance();
        // here we have created new array list and added data to it.
        jobArrayList = new ArrayList<>();

        loadingDialog.startLoadingDialog();

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
                                String jobName = (String) job.get("jobType");
                                String jobDesc = (String) job.get("description");
                                String jobLocation = (String) job.get("location");
                                String jobWage = (String) job.get("wage");
                                String jobId = (String) document.getId();
                                String companyId = (String) job.get("companyId");

                                getCompanyDetails(companyId);

                                String startdate = (String) job.get("startDate");
                                String enddate = (String) job.get("endDate");
                                String startime = (String) job.get("startTime");
                                String endtime = (String) job.get("endTime");
                                String required_workers = (String) job.get("required_workers");
                                String shortage = (String) job.get("vacancy");
                                String contact = (String) job.get("contact");

                                System.out.println("CFB: "+companyName);
                                jobArrayList.add(new jobDetails(jobName, jobDesc, jobLocation, R.drawable.jobimage,jobWage,jobId,
                                        companyId,companyName,startdate, enddate, startime, endtime, required_workers,
                                        shortage, contact));


                                System.out.println(("JOb Array"+ jobArrayList));
                            }
                        } else {
                            System.out.println("Error getting documents: "+task.getException());
                        }

                        setJobArray(jobArrayList);
                        loadingDialog.dismissDialog();
                    }
                });
    }
    private void setJobArray(ArrayList<jobDetails> jobList) {
        JobAdapter courseAdapter = new JobAdapter(getActivity(), jobList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

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
        String item1 = (String) item.getTitle();
        System.out.println("Locitem"+locItem);
        System.out.println("JobItem"+jobItem);
        if(!item1.equals("Location") && !item1.equals("JobType")){


            filter((String) item.getTitle());
            return super.onOptionsItemSelected(item);
        }else
        {
            clickedMenu = item1;
        }

        return super.onOptionsItemSelected(item);
    }

    private void filter(String title) {

        if(clickedMenu.equals("Location"))
        {
            ArrayList<jobDetails> filteredlist = new ArrayList<>();
            if(title.equals("Clear"))
            {
                locItem="";
                if(jobItem.equals("") || jobItem.equals("Clear"))
                {
                    for (jobDetails item : jobArrayList) {

                            filteredlist.add(item);
                    }
                    setJobArray(filteredlist);
                }else{
//                    ArrayList<jobDetails> filteredlist1 = new ArrayList<>();
                    for (jobDetails item : jobArrayList) {

                        if (item.getJob_name().toLowerCase().contains(jobItem.toLowerCase())) {

                            filteredlist.add(item);
                        }
                    }if (filteredlist.isEmpty()) {

                        Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                    } else {

                        System.out.println("Filtered list "+filteredlist.get(0));

                        setJobArray(filteredlist);
                        System.out.println("Hello");
                    }
                }
            }else {
                for (jobDetails item : jobArrayList) {
                    // checking if the entered string matched with any item of our recycler view.
                    //System.out.println(item.getJob_name());
                    if (item.getJob_location().toLowerCase().contains(title.toLowerCase())) {
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

                    locItem = title;
                    if (jobItem.equals("")) {
                        setJobArray(filteredlist);
                        System.out.println("Hello");
                    } else {
                        ArrayList<jobDetails> filteredlist1 = new ArrayList<>();
                        for (jobDetails item : filteredlist) {

                            if (item.getJob_name().toLowerCase().contains(jobItem.toLowerCase())) {

                                filteredlist1.add(item);
                            }
                        }
                        if (filteredlist1.isEmpty()) {

                            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                        } else {

//                        System.out.println("Filtered list "+filteredlist.get(0));

                            setJobArray(filteredlist1);
                            System.out.println("Hello");
                        }
                    }
                }

            }
            }

        else if(clickedMenu.equals("JobType"))
        {
            ArrayList<jobDetails> filteredlist = new ArrayList<>();
            if(title.equals("Clear") || jobItem.equals("Clear"))
            {
                jobItem ="";
                if(locItem.equals(""))
                {
                    for (jobDetails item : jobArrayList) {

                        filteredlist.add(item);
                    }
                    setJobArray(filteredlist);
                }else{

                    for (jobDetails item : jobArrayList) {

                        if (item.getJob_location().toLowerCase().contains(locItem.toLowerCase())) {

                            filteredlist.add(item);
                        }
                    }if (filteredlist.isEmpty()) {

                        Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                    } else {

//                        System.out.println("Filtered list "+filteredlist.get(0));

                        setJobArray(filteredlist);
                        System.out.println("Hello");
                    }
                }
            }else {
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

                    jobItem = title;
                    if (locItem.equals("")) {
                        setJobArray(filteredlist);
                        System.out.println("Hello");
                    } else {
                        ArrayList<jobDetails> filteredlist2 = new ArrayList<>();
                        for (jobDetails item : filteredlist) {

                            if (item.getJob_location().toLowerCase().contains(locItem.toLowerCase())) {

                                filteredlist2.add(item);
                            }
                        }
                        if (filteredlist2.isEmpty()) {

                            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                        } else {



                            setJobArray(filteredlist2);
                            System.out.println("Hello");
                        }
                    }
                }
            }

        }
        else{
            setJobArray(jobArrayList);
        }
    }
        // running a for loop to compare elements.
//        for (jobDetails item : jobArrayList) {
//            // checking if the entered string matched with any item of our recycler view.
//            //System.out.println(item.getJob_name());
//            if (item.getJob_name().toLowerCase().contains(title.toLowerCase())) {
//                // if the item is matched we are
//                // adding it to our filtered list.
//                filteredlist.add(item);
//            }
//        }
//
//        if (filteredlist.isEmpty()) {
//            // if no item is added in filtered list we are
//            // displaying a toast message as no data found.
//            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            // at last we are passing that filtered
//            // list to our adapter class.
//            System.out.println("Filtered list "+filteredlist.get(0));
//
//            setJobArray(filteredlist);
//            System.out.println("Hello");
//        }



    private void getCompanyDetails(String companyId) {


        DocumentReference docRef1 = db.collection("employer_details").document(companyId);
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> job = document.getData();
                        companyName = (String) job.get("companyName");
                        System.out.println("CN: "+companyName);
                    }
                }
                else{
                    companyName = "false";
                    System.out.println("TASK FAILED");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Listener Failed");
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        System.out.println("Task Cancelled");
                    }
                });

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