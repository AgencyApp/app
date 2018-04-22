package com.theagencyapp.world.Activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.AddEmployeesRecyclerViewAdapter;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.ClassModel.Employee_Display;
import com.theagencyapp.world.ClassModel.Team;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.R;

import java.util.ArrayList;

public class AddTeam extends AppCompatActivity {

    private ArrayList<String> selectedEmployees;//contains Employee id of selected employee
    private ArrayList<Employee_Display> employee_displays;//contains Employee to be displayed
    private EditText teamTitle;
    private FirebaseDatabase firebaseDatabase;
    private AddEmployeesRecyclerViewAdapter adapter;
    private ProgressBar employeesLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        selectedEmployees = new ArrayList<>();
        employee_displays=new ArrayList<>();
        teamTitle = findViewById(R.id.add_team_title);
        firebaseDatabase=FirebaseDatabase.getInstance();
        FetchEmployee();//fills Employee Display
        //adding dummy data for testing
        ArrayList<String> skills = new ArrayList<>();
        skills.add("HTML");
        //employee_displays.add(new Employee_Display("Ahmad",null,null,null,skills,null,"AXR"));

        employeesLoading = findViewById(R.id.loadingEmployees);
        employeesLoading.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = findViewById(R.id.employee_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddEmployeesRecyclerViewAdapter(employee_displays);
        recyclerView.setAdapter(adapter);

        //selectedEmployees.add("DFKH9cun7iTotgIUzJCvsBS0cIt2");
        //selectedEmployees.add("49CJyarycKX36r4VVSBMJG7ARbd2");
    }

    private void AddTeam() {
        Team team = new Team(teamTitle.getText().toString(), selectedEmployees);
        DatabaseReference databaseReference=firebaseDatabase.getReference("Teams").push();
        databaseReference.setValue(team);
        final String key=databaseReference.getKey();
        DatabaseReference agid= firebaseDatabase.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                firebaseDatabase.getReference("AgencyTeamRef").child(agencyid).child(key).setValue(true);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_action, menu);
        return true;
    }

    private void FetchEmployee()
    {
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference agid= FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                DatabaseReference clients=firebaseDatabase.getReference("AgencyEmpRef/"+agencyid);
                clients.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.getValue(boolean.class))
                            {
                                fetchEmployeeData(snapshot.getKey());
                            }
                        }



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


    private void fetchEmployeeData(final String employeeId)
    {
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference user=firebaseDatabase.getReference("Users/"+employeeId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user =dataSnapshot.getValue(User.class);
                DatabaseReference employee=firebaseDatabase.getReference("Employees/"+employeeId);
                employee.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employee employee=dataSnapshot.getValue(Employee.class);
                        employee_displays.add(new Employee_Display(user.getName(),user.getPhoneNo(),user.getAgencyid(),user.getStatus(),employee.getSkills(),employee.getImageUrl(),dataSnapshot.getKey()));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_icon:

                String teamName = ((TextView) findViewById(R.id.add_team_title)).getText().toString();
                if (TextUtils.isEmpty(teamName)) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.add_team_main), "Enter team name", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    return true;
                }

                selectedEmployees = adapter.getSelectedEmployees();
                if (selectedEmployees == null) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.add_team_main), "Select at least one member", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    return true;
                }


                Toast.makeText(this, "Creating Team", Toast.LENGTH_SHORT).show();
                AddTeam();
                finish();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
