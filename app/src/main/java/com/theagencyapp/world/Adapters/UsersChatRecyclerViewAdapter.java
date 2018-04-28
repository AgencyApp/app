package com.theagencyapp.world.Adapters;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UsersChatRecyclerViewAdapter extends RecyclerView.Adapter<UsersChatRecyclerViewAdapter.ViewHolder> {

    private final List<LastMessage> mValues;
    private final List<String> mIds;
    OnListFragmentInteractionListener mListener;


    public UsersChatRecyclerViewAdapter(List<LastMessage> items, List<String> ids, OnListFragmentInteractionListener mListener) {
        mValues = items;
        this.mListener = mListener;
        mIds = ids;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getReciverName());
        holder.mLastMessage.setText(mValues.get(position).getLastMessage());
        Date date = new Date(mValues.get(position).getTimeStamp() * 1000);

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        holder.mTimeStamp.setText(sdf.format(date));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("receiverUid", mIds.get(position));
                bundle.putString("receiverName", holder.mItem.getReciverName());
                mListener.onListFragmentInteraction(bundle, "chatMessage", true);
            }
        });


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
