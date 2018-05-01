package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.ClassModel.Employee_Display;
import com.theagencyapp.world.ClassModel.Team;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Adapters.TeamMembersRecyclerViewAdapter;

import java.util.ArrayList;

public class TeamDetailsActivity extends AppCompatActivity {

    private TeamMembersRecyclerViewAdapter adapter;
    private ArrayList<Employee_Display> employee_displays;//contains Employee to be displayed
    private ProgressBar employeesLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_details);


        Intent intent = getIntent();
        Bundle details = intent.getBundleExtra("details");
        String teamId = details.getString("team_id");
        employee_displays = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.team_members_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamMembersRecyclerViewAdapter(employee_displays);
        recyclerView.setAdapter(adapter);
        employeesLoading = findViewById(R.id.loadingEmployees);

        FetchTeamMembers(teamId);
    }

    private void FetchTeamMembers(final String teamId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference clients = firebaseDatabase.getReference("Teams/" + teamId);
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Team team = dataSnapshot.getValue(Team.class);
                if (team != null) {
                    for (String emp : team.getEmployeeId())
                        fetchEmployeeData(emp);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void fetchEmployeeData(final String employeeId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference user = firebaseDatabase.getReference("Users/" + employeeId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                DatabaseReference employee = firebaseDatabase.getReference("Employees/" + employeeId);
                employee.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        employee_displays.add(new Employee_Display(user.getName(), user.getPhoneNo(), user.getAgencyid(), user.getStatus(), employee.getSkills(), employee.getImageUrl(), dataSnapshot.getKey()));
                        employeesLoading.setVisibility(View.GONE);
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
}
