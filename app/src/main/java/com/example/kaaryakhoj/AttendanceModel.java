package com.example.kaaryakhoj;

public class AttendanceModel {

    String att_date, check_in_time , check_out_time, on_time;

    public AttendanceModel(String att_date, String check_in_time, String check_out_time, String on_time) {
        this.att_date = att_date;
        this.check_in_time = check_in_time;
        this.check_out_time = check_out_time;
        this.on_time = on_time;
    }

    public String getAtt_date() {
        return att_date;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public String getOn_time() {
        return on_time;
    }
}
