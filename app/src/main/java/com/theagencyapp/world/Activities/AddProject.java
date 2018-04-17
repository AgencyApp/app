package com.theagencyapp.world.Activities;

import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.theagencyapp.world.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AddProject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edittext;
    final Calendar myCalendar = Calendar.getInstance();
    ImageButton high;
    ImageButton medium;
    ImageButton low;

    String priority = null;
    int clientSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


        edittext = findViewById(R.id.add_project_deadline);
        high = findViewById(R.id.priority_high);
        medium = findViewById(R.id.priority_medium);
        low = findViewById(R.id.priority_low);
        Spinner sp = findViewById(R.id.clients_spinner);

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

        edittext.setOnClickListener(new View.OnClickListener() {

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

        edittext.setText(sdf.format(myCalendar.getTime()));
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
}
