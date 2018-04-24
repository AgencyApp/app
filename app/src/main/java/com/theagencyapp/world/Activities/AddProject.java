package com.theagencyapp.world.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.Client_Display;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.ClassModel.Employee_Display;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.ClassModel.Team;
import com.theagencyapp.world.ClassModel.Team_Display;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.R;
//import com.theagencyapp.world.Utility.Fetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AddProject extends AppCompatActivity {

    private EditText projectDeadline;
    final Calendar myCalendar = Calendar.getInstance();
    ImageButton high;
    ImageButton medium;
    ImageButton low;
    EditText description;
    EditText title;
    ArrayList<Client_Display> client_displays;
    ArrayList<Team_Display>teams;
    String priority = null;
    int clientSelected = 0;
    int teamSelected = 0;
    FirebaseDatabase firebaseDatabase;
    String teamId;
    String clientId;//dummy data has been added in on create
    ArrayAdapter<Client_Display> clientSpinnerAdapter;
    ArrayAdapter<Team_Display> teamSpinnerAdapter;
    String agencyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        setTitle("Add Project");

        projectDeadline = findViewById(R.id.add_project_deadline);
        high = findViewById(R.id.priority_high);
        medium = findViewById(R.id.priority_medium);
        low = findViewById(R.id.priority_low);
        Spinner clientSpinner = findViewById(R.id.clients_spinner);
        Spinner teamSpinner = findViewById(R.id.teams_spinner);
        description = findViewById(R.id.add_project_description);
        title = findViewById(R.id.add_project_title);
        client_displays=new ArrayList<>();
        teams=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();

        client_displays.add(new Client_Display("No Client", null, null, null, 0, null, null));
        teams.add(new Team_Display("No Team", null, null));

        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        agencyid = sharedPreferences.getString("agency_id", "h");

        clientSpinnerAdapter = new ArrayAdapter<Client_Display>(this, android.R.layout.simple_spinner_dropdown_item, client_displays);
        clientSpinner.setAdapter(clientSpinnerAdapter);

        teamSpinnerAdapter = new ArrayAdapter<Team_Display>(this, android.R.layout.simple_spinner_dropdown_item, teams);
        teamSpinner.setAdapter(teamSpinnerAdapter);

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teamSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clientSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        projectDeadline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddProject.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        FetchClient();
        FetchTeam();

        //teamId="LAPruTctW5uooP8KSNC";//dummy data
        //clientId="dYWGvODOIhS2scA2ZKu3lUW9Esh1";//dummy data
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        projectDeadline.setText(sdf.format(myCalendar.getTime()));
    }

    public void onHighPriorityClick(View view) {
        high.setImageResource(R.drawable.fire_checked);
        medium.setImageResource(R.drawable.drop);
        low.setImageResource(R.drawable.leaf);
        priority = "high";
    }

    public void onMediumPriorityClick(View view) {
        high.setImageResource(R.drawable.fire);
        medium.setImageResource(R.drawable.drop_checked);
        low.setImageResource(R.drawable.leaf);
        priority = "medium";
    }

    public void onLowPriorityClick(View view) {
        high.setImageResource(R.drawable.fire);
        medium.setImageResource(R.drawable.drop);
        low.setImageResource(R.drawable.leaf_checked);
        priority = "low";
    }




    private void FetchClient()
    {

        final DatabaseReference clients = firebaseDatabase.getReference("AgencyClientRef/" + agencyid);
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(boolean.class)) {
                        fetchClientData(snapshot.getKey());
                    }
                }

                //onDatasetnotify();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchClientData(final String clientId)
    {
        DatabaseReference user=firebaseDatabase.getReference("Users/"+clientId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user =dataSnapshot.getValue(User.class);
                final DataSnapshot temp=dataSnapshot;
                DatabaseReference client=firebaseDatabase.getReference("Clients/"+clientId);
                client.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Client client=dataSnapshot.getValue(Client.class);
                        if(client!=null)
                        client_displays.add(new Client_Display(user.getName(),user.getPhoneNo(),user.getAgencyid(),user.getStatus(),client.getRatings(),temp.getKey(),client.getImageUrl()));
                        clientSpinnerAdapter.notifyDataSetChanged();
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

    private void FetchTeam()
    {

        final DatabaseReference clients = firebaseDatabase.getReference("AgencyTeamRef/" + agencyid);
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(boolean.class)) {
                        fetchTeamData(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchTeamData(final String teamId)
    {
        DatabaseReference user=firebaseDatabase.getReference("Teams/"+teamId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                teams.add(new Team_Display(team.getName(),team.getEmployeeId(),dataSnapshot.getKey()));
                teamSpinnerAdapter.notifyDataSetChanged();
                Log.d("team_id",dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addTeam(View view)
    {
        Intent i=new Intent(this,AddTeam.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_icon:
                OnDone();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void OnDone() {
        if (priority == null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_layout_id), "Fill all the fields", Snackbar.LENGTH_LONG);


            snackbar.show();
            return;
        }
        clientId = client_displays.get(clientSelected).getClient_id();
        teamId = teams.get(teamSelected).getTeamId();

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String agencyId = sharedPreferences.getString("agency_id", "h");
        DatabaseReference databaseReference = firebaseDatabase.getReference("MilestoneContainer").push();
        String mileStoneContainer = databaseReference.getKey();
        Project project = new Project(title.getText().toString(), mileStoneContainer, clientId, teamId, priority, projectDeadline.getText().toString(), description.getText().toString());
        databaseReference = firebaseDatabase.getReference("Projects").push();
        String key = databaseReference.getKey();
        firebaseDatabase.getReference("ProjectRefTable/" + agencyId).child(key).setValue(true);
        databaseReference.setValue(project);
        Toast.makeText(this, "Adding Project", Toast.LENGTH_SHORT).show();
        finish();


    }



}
