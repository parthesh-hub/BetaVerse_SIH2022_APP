package com.example.kaaryakhoj;

import android.accessibilityservice.GestureDescription;

public class jobDetails {


    private String job_name,job_desc;
    private String job_location;
    private int job_image;
    private String wage;
    private String job_id;

    public jobDetails(String job_name, String job_desc, String job_location, int job_image, String wage, String job_id) {
        this.job_name = job_name;
        this.job_desc = job_desc;
        this.job_location = job_location;
        this.job_image = job_image;
        this.wage = wage;
        this.job_id = job_id;
    }

    public String getJob_name(){
        return job_name;
    }
    public void setJob_name(String job_name){
        this.job_name = job_name;

    }
    public String getJob_location(){
        return job_location;
    }
    public void setJob_location(String job_location){
        this.job_location = job_location;

    }
    public String getJob_id(){
        return job_id;
    }
    public void setJob_image(String job_id){
        this.job_id = job_id;

    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public int getJob_image(){
        return job_image;
    }
    public void setJob_image(int job_image){
        this.job_image = job_image;

    }

    @Override
    public String toString() {
        return "JobModel{" +
                "jobName='" + job_name + '\'' +
                ", jobLocation=" +job_location+
                ", jobDesc='" + job_desc + '\'' +
               ", wage=" +wage+
                '}';


    }
}
