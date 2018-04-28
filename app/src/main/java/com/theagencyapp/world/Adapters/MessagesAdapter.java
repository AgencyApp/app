package com.theagencyapp.world.Adapters;

/**
 * Created by abdul on 4/26/2018.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theagencyapp.world.Activities.SendMessage;
import com.theagencyapp.world.Activities.ShowMap;
import com.theagencyapp.world.ClassModel.Message;
import com.theagencyapp.world.ClassModel.User_Display;
import com.theagencyapp.world.R;


import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;


    private final String ownerUid;
    private final Context context;
    private final ArrayList<Message> messages;

    public MessagesAdapter(Context context, String ownerUid, ArrayList<Message> messages) {
        this.context = context;
        this.ownerUid = ownerUid;
        this.messages = messages;
    }


    @Override
    public void onBindViewHolder(final MessagesAdapter.MessageViewHolder holder, int position) {
        holder.mItem = messages.get(position);
        if (!messages.get(position).isMap())
            holder.mText.setText(messages.get(position).getMessage());
        else {
            holder.mText.setText("Tap to View Location");
            holder.mText.setTextColor(Color.YELLOW);
            String message = messages.get(position).getMessage();
            final String[] coords = message.split(",");
            holder.mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowMap.class);
                    intent.putExtra("lat", Double.parseDouble(coords[0]));
                    intent.putExtra("lng", Double.parseDouble(coords[1]));
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderUid().equals(ownerUid)) {

            return VIEW_TYPE_SENT;

        } else {

            return VIEW_TYPE_RECEIVED;

        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_SENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                break;
            case VIEW_TYPE_RECEIVED:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
        }
        return new MessageViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        public final TextView mText;
        public Message mItem;


        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            mText = view.findViewById(R.id.item_message_body_text_view);
        }


    }


}

