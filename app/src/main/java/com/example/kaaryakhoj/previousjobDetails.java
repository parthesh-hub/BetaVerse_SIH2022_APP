package com.example.kaaryakhoj;

public class previousjobDetails {

    private String job_name,job_desc;
    private String job_location;
    private int job_image;
    private String wage;
    private String job_id;
    // newly added
    private String company_id, companyname ;
    private String startdate, enddate, startime, endtime, required_workers, shortage, contact ;

    public previousjobDetails(String job_name, String job_desc, String job_location, int job_image,
                              String wage, String job_id, String company_id, String companyname,
                              String startdate, String enddate, String startime, String endtime,
                              String required_workers, String shortage, String contact) {
        this.job_name = job_name;
        this.job_desc = job_desc;
        this.job_location = job_location;
        this.job_image = job_image;
        this.wage = wage;
        this.job_id = job_id;
        this.company_id = company_id;
        this.companyname = companyname;
        this.startdate = startdate;
        this.enddate = enddate;
        this.startime = startime;
        this.endtime = endtime;
        this.required_workers = required_workers;
        this.shortage = shortage;
        this.contact = contact;
    }


    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public int getJob_image() {
        return job_image;
    }

    public void setJob_image(int job_image) {
        this.job_image = job_image;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStartime() {
        return startime;
    }

    public void setStartime(String startime) {
        this.startime = startime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRequired_workers() {
        return required_workers;
    }

    public void setRequired_workers(String required_workers) {
        this.required_workers = required_workers;
    }

    public String getShortage() {
        return shortage;
    }

    public void setShortage(String shortage) {
        this.shortage = shortage;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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
