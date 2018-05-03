package com.theagencyapp.world.Adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.ClassModel.AttendanceDisplay;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Utility.ProfilePicture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hamza on 01-May-18.
 */

public class AttendanceLogRC extends RecyclerView.Adapter<AttendanceLogRC.ViewHolder> {

    private final List<AttendanceDisplay> mValues;



    public AttendanceLogRC(List<AttendanceDisplay> items) {
        mValues = items;

    }

    @Override
    public AttendanceLogRC.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item, parent, false);
        return new AttendanceLogRC.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AttendanceLogRC.ViewHolder holder, final int position) {
        holder.mItem=mValues.get(position);
        holder.mName.setText(mValues.get(position).getEmployeeName());
        Date date = new Date(mValues.get(position).getTimeStamp() * 1000);

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        holder.mTimeStamp.setText(sdf.format(date));

        ProfilePicture.setProfilePicture(mValues.get(position).getEmployeeId(), holder.mPicture);


    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mName;
        public final TextView mTimeStamp;
        public AttendanceDisplay mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = view.findViewById(R.id.Attendence_User_dp);
            mName = view.findViewById(R.id.Attendence_Employee_Name);
            mTimeStamp = view.findViewById(R.id.Attendance_timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
