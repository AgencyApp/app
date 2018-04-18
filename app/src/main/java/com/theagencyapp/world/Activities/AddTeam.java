package com.theagencyapp.world.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class AddTeam extends AppCompatActivity {

    ArrayList<String>selectedEmployee;//contains Employee id of selected employee
    ArrayList<Employee_Display>employee_displays;//contains Employee to be displayed
    EditText teamTitle;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        selectedEmployee=new ArrayList<>();
        employee_displays=new ArrayList<>();
        teamTitle=(EditText)findViewById(R.id.add_team_title);
        firebaseDatabase=FirebaseDatabase.getInstance();
        FetchEmployee();//fills Employee Display
        //adding dummy data for testing
        selectedEmployee.add("DFKH9cun7iTotgIUzJCvsBS0cIt2");
        selectedEmployee.add("49CJyarycKX36r4VVSBMJG7ARbd2");
    }

    public void onDone (View view){
        Team team=new Team(teamTitle.getText().toString(),selectedEmployee);
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

    public  void FetchEmployee()
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

                        //ON Data Set Change

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

    void  fetchEmployeeData(final String employeeId)
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
