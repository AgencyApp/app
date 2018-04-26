package com.theagencyapp.world.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.Adapters.AddEmployeesRecyclerViewAdapter;
import com.theagencyapp.world.Adapters.AllUsersChatRecyclerViewAdapter;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.ClassModel.Employee_Display;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.ClassModel.User_Display;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.util.ArrayList;

/**
 * Created by abdul on 4/26/2018.
 */

public class AllUsersForChat extends AppCompatActivity implements OnListFragmentInteractionListener {

    private ArrayList<User_Display> users;
    private FirebaseDatabase firebaseDatabase;
    private AllUsersChatRecyclerViewAdapter adapter;
    OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_for_chat);

        users = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mListener = this;
        RecyclerView recyclerView = findViewById(R.id.users_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllUsersChatRecyclerViewAdapter(users, this, mListener);
        recyclerView.setAdapter(adapter);

        FetchEmployees();
        FetchClients();

    }


    private void FetchEmployees() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String agencyid = sharedPreferences.getString("agency_id", "h");

        DatabaseReference clients = firebaseDatabase.getReference("AgencyEmpRef/" + agencyid);
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(boolean.class)) {
                        fetchEmployeeData(snapshot.getKey());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void FetchClients() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String agencyid = sharedPreferences.getString("agency_id", "h");

        DatabaseReference clients = firebaseDatabase.getReference("AgencyClientRef/" + agencyid);
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(boolean.class)) {
                        fetchClientData(snapshot.getKey());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void fetchEmployeeData(final String userId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference user = firebaseDatabase.getReference("Users/" + userId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                DatabaseReference emp = firebaseDatabase.getReference("Employees/" + userId);
                emp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        users.add(new User_Display(user.getName(), user.getPhoneNo(), user.getAgencyid(), user.getStatus(), userId, employee.getImageUrl()));
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void fetchClientData(final String userId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference user = firebaseDatabase.getReference("Users/" + userId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                DatabaseReference emp = firebaseDatabase.getReference("Clients/" + userId);
                emp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Client client = dataSnapshot.getValue(Client.class);
                        users.add(new User_Display(user.getName(), user.getPhoneNo(), user.getAgencyid(), user.getStatus(), userId, client.getImageUrl()));
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


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
