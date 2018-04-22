package com.theagencyapp.world;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.theagencyapp.world.Activities.AddTeam.onEmployeeTapListener;
import com.theagencyapp.world.ClassModel.Employee_Display;


import java.util.ArrayList;
import java.util.List;


public class AddEmployeesRecyclerViewAdapter extends RecyclerView.Adapter<AddEmployeesRecyclerViewAdapter.ViewHolder> {

    private final List<Employee_Display> mValues;
    private MultiSelector mMultiSelector = new MultiSelector();
    private ArrayList<String> selectedEmployeeIds = new ArrayList<>();


    public AddEmployeesRecyclerViewAdapter(List<Employee_Display> items) {
        mValues = items;
        mMultiSelector.setSelectable(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_row, parent, false);
        return new ViewHolder(view, mMultiSelector);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());
        holder.mSkills.setText(mValues.get(position).getSkillString());

        //TODO: Set Bitmap for Employee


    }

    public ArrayList<String> getSelectedEmployees() {
        List<Integer> selectedPositions = mMultiSelector.getSelectedPositions();
        ArrayList<String> ids = new ArrayList<>();

        if (selectedPositions == null || selectedPositions.size() == 0) {
            return null;
        }

        for (int position : selectedPositions) {
            ids.add(mValues.get(position).getEmployee_id());
        }

        return ids;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener

    {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mName;
        public final TextView mSkills;
        public Employee_Display mItem;
        boolean isSelected;


        public ViewHolder(View view, MultiSelector mMultiSelector) {
            super(view, mMultiSelector);
            mView = view;
            mPicture = view.findViewById(R.id.employee_pic);
            mName = view.findViewById(R.id.employee_name);
            mSkills = view.findViewById(R.id.employee_skills);
            isSelected = false;
            view.setOnClickListener(this);
            //super.setSelectionModeBackgroundDrawable(new ColorDrawable(mView.getContext().getResources().getColor(R.color.colorPrimary)));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            mMultiSelector.setSelected(ViewHolder.this, isSelected = !isSelected);
        }


        @Override
        public boolean onLongClick(View view) {
            if (!mMultiSelector.isSelectable()) { // (3)
                mMultiSelector.setSelectable(true); // (4)
                //mMultiSelector.setSelected(MyViewHolder.this, true); // (5)
                return true;
            }
            return false;

        }


    }
}
