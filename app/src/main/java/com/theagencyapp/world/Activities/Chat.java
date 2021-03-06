package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.Adapters.ClientsRecyclerViewAdapter;
import com.theagencyapp.world.Adapters.UsersChatRecyclerViewAdapter;
import com.theagencyapp.world.ClassModel.LastMessage;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class Chat extends AppCompatActivity implements OnListFragmentInteractionListener {

    ArrayList<LastMessage>lastMessages;
    ArrayList<String> lastMessagesReceiverIds;
    FirebaseDatabase firebaseDatabase;
    String currentUid;
    User currentUser;
    private RecyclerView recyclerView;
    private UsersChatRecyclerViewAdapter adapter;
    OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lastMessages=new ArrayList<>();
        lastMessagesReceiverIds = new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.users_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter = new UsersChatRecyclerViewAdapter(lastMessages, lastMessagesReceiverIds, mListener = this));

        FloatingActionButton myFab = findViewById(R.id.newChat);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Chat.this, AllUsersForChat.class));
            }
        });
        getCurrentUserData();

        setTitle("Chat");
        //updateUI();
        //start loading bar
    }

    void updateUI()
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("CurrentChat/"+currentUid);
        databaseReference.orderByChild("timeStamp").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                LastMessage lastMessage = dataSnapshot.getValue(LastMessage.class);
                lastMessages.add(0, lastMessage);
                lastMessagesReceiverIds.add(0, dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void getCurrentUserData()
    {
        DatabaseReference userRef=firebaseDatabase.getReference("Users/"+currentUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser=dataSnapshot.getValue(User.class);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra("receiverUid", details.getString("receiverUid"));
        intent.putExtra("receiverName", details.getString("receiverName"));

        startActivity(intent);
    }
}
