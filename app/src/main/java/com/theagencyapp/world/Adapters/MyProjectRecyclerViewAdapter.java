package com.theagencyapp.world.Adapters;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.util.ArrayList;
import java.util.List;


public class MyProjectRecyclerViewAdapter extends RecyclerView.Adapter<MyProjectRecyclerViewAdapter.ViewHolder> {

    private final List<Project> mValues;
    private final OnListFragmentInteractionListener mListener;
    private TextDrawable.IBuilder builder;

    public MyProjectRecyclerViewAdapter(ArrayList<Project> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(1)
                .endConfig()
                .rect();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mProject = mValues.get(position);
        holder.mProjectName.setText(mValues.get(position).getName());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mValues.get(position).getName());
        TextDrawable ic1 = builder.build(mValues.get(position).getName().toUpperCase().substring(0, 1), color);
        holder.mProjectIcon.setImageDrawable(ic1);

        String priority = mValues.get(position).getPriority();
        int id = R.drawable.fire;
        switch (priority) {
            case "high":
                id = R.drawable.fire;
                break;
            case "medium":
                id = R.drawable.drop;
                break;
            case "low":
                id = R.drawable.leaf;
                break;

        }
        holder.mProjectPriority.setImageDrawable(ContextCompat.getDrawable(holder.mProjectIcon.getContext(), id));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Bundle bundle = new Bundle();
                    bundle.putString("project_name", holder.mProject.getName());
                    bundle.putString("milestones_container_id", holder.mProject.getMileStoneContainer());
                    bundle.putString("team_id", holder.mProject.getTeamId());
                    bundle.putString("client_id", holder.mProject.getClientId());
                    bundle.putString("priority", holder.mProject.getPriority());
                    bundle.putString("description", holder.mProject.getDescription());
                    bundle.putString("deadline", holder.mProject.getDeadline());
                    mListener.onListFragmentInteraction(bundle, "ProjectDetails", false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mProjectIcon;
        public final TextView mProjectName;
        public final ImageView mProjectPriority;
        public Project mProject;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProjectIcon = view.findViewById(R.id.project_icon);
            mProjectName = view.findViewById(R.id.project_name);
            mProjectPriority = view.findViewById(R.id.project_priority);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mProjectName.getText() + "'";
        }
    }
}
