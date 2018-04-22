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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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


public class AddProject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    FirebaseDatabase firebaseDatabase;
    String teamId;
    String clientId;//dummy data has been added in on creat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        setTitle("Add Project");

        projectDeadline = findViewById(R.id.add_project_deadline);
        high = findViewById(R.id.priority_high);
        medium = findViewById(R.id.priority_medium);
        low = findViewById(R.id.priority_low);
        Spinner sp = findViewById(R.id.clients_spinner);
        description = findViewById(R.id.add_project_description);
        title = findViewById(R.id.add_project_title);
        client_displays=new ArrayList<>();
        teams=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        ArrayList<String> data = new ArrayList<>();
        data.add("No Client");
        data.add("Textra Software Solutions");
        data.add("Netsoft Solutions");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(this);

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

        teamId="LAPruTctW5uooP8KSNC";//dummy data
        clientId="dYWGvODOIhS2scA2ZKu3lUW9Esh1";//dummy data
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

    public void onDoneClick(View view) {
        if (priority == null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_layout_id), "No Priority Selected!", Snackbar.LENGTH_LONG);


            snackbar.show();
            return;
        }
        if(clientId!=null&&teamId!=null)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("data",Context.MODE_PRIVATE);
            String agencyId=sharedPreferences.getString("agency_id","h");
            DatabaseReference databaseReference=firebaseDatabase.getReference("MilestoneContainer").push();
            String mileStoneContainer=databaseReference.getKey();
            Project project=new Project(title.getText().toString(),mileStoneContainer,clientId,teamId,priority,projectDeadline.getText().toString(),description.getText().toString());
            databaseReference=firebaseDatabase.getReference("Projects").push();
            String key=databaseReference.getKey();
            firebaseDatabase.getReference("ProjectRefTable/"+agencyId).child(key).setValue(true);
            databaseReference.setValue(project);
            finish();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.clientSelected = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void FetchClient()
    {

        DatabaseReference agid= firebaseDatabase.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                final DatabaseReference clients=firebaseDatabase.getReference("AgencyClientRef/"+agencyid);
                clients.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.getValue(boolean.class))
                            {
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

        DatabaseReference agid= firebaseDatabase.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                final DatabaseReference clients=firebaseDatabase.getReference("AgencyTeamRef/"+agencyid);
                clients.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.getValue(boolean.class))
                            {
                                fetchTeamData(snapshot.getKey());
                            }
                        }

                        //onDatasetnotify();

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

    private void fetchTeamData(final String teamId)
    {
        DatabaseReference user=firebaseDatabase.getReference("Teams/"+teamId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                teams.add(new Team_Display(team.getName(),team.getEmployeeId(),dataSnapshot.getKey()));
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

}
