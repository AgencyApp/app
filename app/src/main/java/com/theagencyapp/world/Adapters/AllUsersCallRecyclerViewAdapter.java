package com.theagencyapp.world.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.ClassModel.User_Display;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Utility.ProfilePicture;

import java.util.ArrayList;


public class AllUsersCallRecyclerViewAdapter extends RecyclerView.Adapter<AllUsersCallRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<User_Display> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;

    public AllUsersCallRecyclerViewAdapter(ArrayList<User_Display> items, Context context, OnListFragmentInteractionListener mListener) {
        mValues = items;
        c = context;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_simple, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("receiverUid", holder.mItem.getUserId());
                bundle.putString("receiverName", holder.mItem.getName());
                mListener.onListFragmentInteraction(bundle, "chatMessage", true);
            }
        });

        ProfilePicture.setProfilePicture(mValues.get(position).getUserId(), holder.mPicture);


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
        public User_Display mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = view.findViewById(R.id.user_dp);
            mName = view.findViewById(R.id.user_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
