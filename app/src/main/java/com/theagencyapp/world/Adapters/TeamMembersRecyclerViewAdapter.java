package com.theagencyapp.world.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.ClassModel.Employee_Display;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Utility.ProfilePicture;

import java.util.List;


public class TeamMembersRecyclerViewAdapter extends RecyclerView.Adapter<TeamMembersRecyclerViewAdapter.ViewHolder> {

    private final List<Employee_Display> mValues;


    public TeamMembersRecyclerViewAdapter(List<Employee_Display> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());
        holder.mSkills.setText(mValues.get(position).getSkillString());

        ProfilePicture.setProfilePicture(mValues.get(position).getEmployee_id(), holder.mPicture);


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
        public final TextView mSkills;
        public Employee_Display mItem;
        boolean isSelected;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = view.findViewById(R.id.employee_pic);
            mName = view.findViewById(R.id.employee_name);
            mSkills = view.findViewById(R.id.employee_skills);
            isSelected = false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
