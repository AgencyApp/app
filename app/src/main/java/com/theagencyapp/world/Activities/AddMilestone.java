package com.theagencyapp.world.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theagencyapp.world.ClassModel.MileStone;
import com.theagencyapp.world.R;

public class AddMilestone extends AppCompatActivity {
    EditText startTime;
    EditText endTime;
    EditText Description;
    EditText Title;
    String containerId;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_milestone);
        containerId=getIntent().getStringExtra("containerId");
        startTime=findViewById(R.id.add_milestone_start_time);
        endTime=findViewById(R.id.add_milestone_endtime);
        Description=findViewById(R.id.add_milestone_Description);
        Title=findViewById(R.id.add_milestone_name);
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    public void onDone(View view)
    {
       DatabaseReference containerReference= firebaseDatabase.getReference("MilestoneContainer/"+containerId);
       DatabaseReference mileStoneRef=firebaseDatabase.getReference("Milestones").push();
       String key=mileStoneRef.getKey();
       containerReference.child(key).setValue(true);
       MileStone mileStone=new MileStone(startTime.getText().toString(),endTime.getText().toString(),Title.getText().toString(),Description.getText().toString());
       mileStoneRef.setValue(mileStone);

    }


}
