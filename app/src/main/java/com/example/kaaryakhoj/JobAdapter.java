package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.Viewholder> {

    private Context context;
    private ArrayList<jobDetails> jobsArrayList;

    // Constructor
    public JobAdapter(Context context, ArrayList<jobDetails> jobsArrayList) {
        this.context = context;
        this.jobsArrayList = jobsArrayList;
    }

//    public void filterList(ArrayList<jobDetails> filterllist) {
//        // below line is to add our filtered
//        // list in our course array list.
//        jobsArrayList = filterllist;
//        // below line is to notify our adapter
//        // as change in recycler view data.
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public JobAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.findjobs_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        jobDetails model = jobsArrayList.get(position);

        holder.jobName.setText(model.getJob_name());
        holder.jobLocation.setText(model.getJob_location());
        holder.jobImage.setImageResource(model.getJob_image());
        holder.companyName.setText(model.getCompanyname());
        holder.startDate.setText(model.getStartdate());
        holder.endDate.setText(model.getEnddate());
        holder.dailyWage.setText(model.getWage());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("JobId",model.getJob_id());
                intent.putExtra("CompanyName",model.getCompanyname());
                context.startActivity(intent);
                //Toast.makeText(context,model.getJob_id(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return jobsArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView jobImage;
        private TextView jobName, jobLocation, companyName, startDate, endDate, dailyWage;
        private String jobId;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            jobImage = itemView.findViewById(R.id.jobImage);
            companyName = itemView.findViewById(R.id.companyName);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            dailyWage = itemView.findViewById(R.id.dailyWage);

        }
    }


}
