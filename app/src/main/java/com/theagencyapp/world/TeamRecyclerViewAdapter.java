package com.theagencyapp.world;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.theagencyapp.world.ClassModel.Team_Display;

import java.util.List;


public class TeamRecyclerViewAdapter extends RecyclerView.Adapter<TeamRecyclerViewAdapter.ViewHolder> {

    private final List<Team_Display> mValues;
    private final OnListFragmentInteractionListener mListener;
    private TextDrawable.IBuilder builder;

    public TeamRecyclerViewAdapter(List<Team_Display> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTeam = mValues.get(position);
        holder.mTeamName.setText(mValues.get(position).getName());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mValues.get(position).getName());
        TextDrawable ic1 = builder.build(mValues.get(position).getName().toUpperCase().substring(0, 1), color);
        holder.mTeamIcon.setImageDrawable(ic1);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mTeam.getTeamId(), "TeamDetails", false);
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
        public final ImageView mTeamIcon;
        public final TextView mTeamName;
        public Team_Display mTeam;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTeamIcon = view.findViewById(R.id.team_icon);
            mTeamName = view.findViewById(R.id.team_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTeamName.getText() + "'";
        }
    }
}
