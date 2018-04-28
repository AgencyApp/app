package com.theagencyapp.world.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.R;

import java.util.List;


public class UsersChatRecyclerViewAdapter extends RecyclerView.Adapter<UsersChatRecyclerViewAdapter.ViewHolder> {

    private final List<LastMessage> mValues;


    public UsersChatRecyclerViewAdapter(List<LastMessage> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getReciverName());
        holder.mLastMessage.setText(mValues.get(position).getLastMessage());
        holder.mTimeStamp.setText(String.valueOf(mValues.get(position).getTimeStamp()));


        //TODO: Set Bitmap for User


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
        public final TextView mLastMessage;
        public final TextView mTimeStamp;
        public LastMessage mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = view.findViewById(R.id.user_dp);
            mName = view.findViewById(R.id.user_name);
            mLastMessage = view.findViewById(R.id.last_message);
            mTimeStamp = view.findViewById(R.id.timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
