package com.example.kaaryakhoj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapterClass extends RecyclerView.Adapter<AttendanceAdapterClass.ViewHolder> {

    Context context;
    List<AttendanceModel> attendancelist;

    public AttendanceAdapterClass(Context context, List<AttendanceModel> attendancelist) {
        this.context = context;
        this.attendancelist = attendancelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.attendance_item_layout,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (attendancelist != null && attendancelist.size()>0){
            AttendanceModel attendance_model = attendancelist.get(position);
            holder.attendace_date.setText(attendance_model.getAtt_date());
            holder.attendance_checkintime.setText(attendance_model.getCheck_in_time());
            holder.attendance_checkouttime.setText(attendance_model.getCheck_out_time());
            holder.attendance_on_time.setText(attendance_model.getOn_time());
        }
        else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        return attendancelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView attendace_date, attendance_checkintime, attendance_checkouttime, attendance_on_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            attendace_date = itemView.findViewById(R.id.attendace_date);
            attendance_checkintime = itemView.findViewById(R.id.attendance_checkintime);
            attendance_checkouttime = itemView.findViewById(R.id.attendance_checkouttime);
            attendance_on_time = itemView.findViewById(R.id.attendance_on_time);

        }
    }
}
