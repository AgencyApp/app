package com.theagencyapp.world.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.Adapters.AttendanceLogRC;
import com.theagencyapp.world.Adapters.UsersChatRecyclerViewAdapter;
import com.theagencyapp.world.ClassModel.Attendance;
import com.theagencyapp.world.ClassModel.AttendanceDisplay;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.util.ArrayList;

public class AttendanceLog extends AppCompatActivity  {

    ArrayList<AttendanceDisplay>data;
    String agencyId;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    AttendanceLogRC adapter;

    OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_log);
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        agencyId = sharedPreferences.getString("agency_id", "h");
        data=new ArrayList<>();
        firebaseDatabase =FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.Attendance_log_rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter = new AttendanceLogRC(data));
        fetchAttendance();
    }

    void fetchAttendance()
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("Attendance/"+agencyId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Attendance attendance=dataSnapshot.getValue(Attendance.class);
                fetchAttendanceDisplay(attendance);
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

    void fetchAttendanceDisplay(final Attendance attendance)
    {
        DatabaseReference userRef=firebaseDatabase.getReference("Users/"+attendance.getEmployeeId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser=dataSnapshot.getValue(User.class);
                data.add(new AttendanceDisplay(attendance.getTimeStamp(),attendance.getEmployeeId(),currentUser.getName()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
