package com.theagencyapp.world.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.MileStone;
import com.theagencyapp.world.R;

public class DisplayMilestone extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    MileStone milestone;
    String milestoneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mile_stone);
        firebaseDatabase=FirebaseDatabase.getInstance();
        milestoneId=getIntent().getStringExtra("milestoneId");//getting mile stone  id through intent
        fetchMilestone(milestoneId);
        //update UI using milestone variable--

    }


    void fetchMilestoneList(String milestoneContainerId) {

        DatabaseReference container = firebaseDatabase.getReference("MilestoneContainer/" + milestoneContainerId);
        container.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(boolean.class)) {
                        fetchMilestone(snapshot.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void fetchMilestone(String milestoneId)
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("Milestones/"+milestoneId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                milestone=dataSnapshot.getValue(MileStone.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
