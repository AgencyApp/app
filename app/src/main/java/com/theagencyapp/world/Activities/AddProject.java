package com.theagencyapp.world.Activities;

import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Utility.Fetcher;

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
    ArrayList<Employee_Display>employee_displays;
    String priority = null;
    int clientSelected = 0;

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
        description=(EditText)findViewById(R.id.add_project_description);
        title=(EditText)findViewById(R.id.add_project_title);

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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.clientSelected = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void FetchClient()
    {
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference agid= FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                DatabaseReference clients=firebaseDatabase.getReference("AgencyClientRef/"+agencyid);
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
    public void fetchClientData(final String clientId)
    {
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference user=firebaseDatabase.getReference("Users/"+clientId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user =dataSnapshot.getValue(User.class);
                DatabaseReference client=firebaseDatabase.getReference("Client/"+clientId);
                client.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Client client=dataSnapshot.getValue(Client.class);
                        client_displays.add(new Client_Display(user.getName(),user.getPhoneNo(),user.getAgencyid(),user.getStatus(),client.getRatings(),client.getImageUrl()));
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
    public  void FetchEmployee()
    {
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference agid= FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid=dataSnapshot.getValue(String.class);
                DatabaseReference clients=firebaseDatabase.getReference("AgencyEmployeeRef/"+agencyid);
                clients.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.getValue(boolean.class))
                            {
                                fetchEmployeeData(snapshot.getKey());
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

   void  fetchEmployeeData(final String employeeId)
   {
       final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
       DatabaseReference user=firebaseDatabase.getReference("Users/"+employeeId);
       user.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               final User user =dataSnapshot.getValue(User.class);
               DatabaseReference employee=firebaseDatabase.getReference("Employee/"+employeeId);
               employee.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       Employee employee=dataSnapshot.getValue(Employee.class);
                       employee_displays.add(new new(user.getName(),user.getPhoneNo(),user.getAgencyid(),user.getStatus(),client.getRatings(),client.getImageUrl()));
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
