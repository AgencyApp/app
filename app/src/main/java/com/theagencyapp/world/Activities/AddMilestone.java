package com.theagencyapp.world.Activities;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theagencyapp.world.ClassModel.MileStone;
import com.theagencyapp.world.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddMilestone extends AppCompatActivity {
    EditText startTime;
    EditText endTime;
    EditText Description;
    EditText Title;
    String containerId;
    FirebaseDatabase firebaseDatabase;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_milestone);
        setTitle(getString(R.string.add_milestone));
        containerId=getIntent().getStringExtra("containerId");
        startTime=findViewById(R.id.add_milestone_start_time);
        endTime=findViewById(R.id.add_milestone_endtime);
        Description=findViewById(R.id.add_milestone_Description);
        Title=findViewById(R.id.add_milestone_name);
        firebaseDatabase=FirebaseDatabase.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                startTime.setText(sdf.format(myCalendar.getTime()));
            }

        };

        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMilestone.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                endTime.setText(sdf.format(myCalendar.getTime()));
            }

        };

        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMilestone.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void onDone(View view)
    {
        String start = startTime.getText().toString();
        String end = endTime.getText().toString();
        String title = Title.getText().toString();
        String description = Description.getText().toString();

        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.add_milestone_layout), R.string.fill_fields, Snackbar.LENGTH_LONG);


            snackbar.show();
            return;
        }

       DatabaseReference containerReference= firebaseDatabase.getReference("MilestoneContainer/"+containerId);
       DatabaseReference mileStoneRef=firebaseDatabase.getReference("Milestones").push();
       String key=mileStoneRef.getKey();
       containerReference.child(key).setValue(true);
       MileStone mileStone=new MileStone(startTime.getText().toString(),endTime.getText().toString(),Title.getText().toString(),Description.getText().toString());
       mileStoneRef.setValue(mileStone);
        finish();

    }

    public void onMicClick(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Intent problem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Description.setText(result.get(0));
            }
        }
    }


}
