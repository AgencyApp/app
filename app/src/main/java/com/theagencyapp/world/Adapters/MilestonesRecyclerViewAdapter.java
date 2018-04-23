package com.theagencyapp.world.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.theagencyapp.world.ClassModel.MileStone;
import com.theagencyapp.world.R;


import java.util.List;


public class MilestonesRecyclerViewAdapter extends RecyclerView.Adapter<MilestonesRecyclerViewAdapter.ViewHolder> {

    private final List<MileStone> mValues;


    public MilestonesRecyclerViewAdapter(List<MileStone> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.milestone_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.milestone = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());
        holder.startDate.setText(mValues.get(position).getStartTime());
        holder.endDate.setText(mValues.get(position).getEndtime());
        holder.description.setText(mValues.get(position).getDescriptions());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView startDate;
        public final TextView endDate;
        public final TextView description;
        public MileStone milestone;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.milestone_title);
            startDate = view.findViewById(R.id.start_date);
            endDate = view.findViewById(R.id.end_date);
            description = view.findViewById(R.id.milestone_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
