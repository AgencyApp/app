package com.theagencyapp.world.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Attendance;
import com.theagencyapp.world.ClassModel.MyLocation;
import com.theagencyapp.world.R;

public class AttendanceSystem extends AppCompatActivity {
    LocationManager mLocationManager;
    Location location;
    MyLocation officeLocation;
    FirebaseDatabase firebaseDatabase;
    Button markAttendanceButton;
    CoordinatorLayout coordinatorLayout;
    TextView error;
    String agencyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_system);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        markAttendanceButton = (Button) findViewById(R.id.MarkAttendence);
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        agencyId = sharedPreferences.getString("agency_id", "h");
        markAttendanceButton.setEnabled(false);
        error = findViewById(R.id.Attendence_Not_Ready);
        firebaseDatabase = FirebaseDatabase.getInstance();
        coordinatorLayout = findViewById(R.id.Attendence_coordinatorLayout);
        getOfficeLocation();
        getPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location l) {
                        location = l;
                        error.setText("Go Ahead Mark Your Attendance!");
                        error.setTextColor(Color.parseColor("#55AA55"));

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        125);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    void getOfficeLocation() {

        DatabaseReference databaseReference = firebaseDatabase.getReference("AgencyLocation").child(agencyId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officeLocation = dataSnapshot.getValue(MyLocation.class);
                markAttendanceButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onMarkAttendance(View view) {
        if (officeLocation != null && location != null) {
            float[] result = new float[3];
            Location.distanceBetween(officeLocation.getLatitude(), officeLocation.getLongitude(), location.getLatitude(), location.getLongitude(), result);
            if (Math.abs(result[0]) < 5) {
                Long ts = System.currentTimeMillis() / 1000;
                Attendance attendance = new Attendance(ts, FirebaseAuth.getInstance().getUid());
                DatabaseReference databaseReference = firebaseDatabase.getReference("Attendance/" + agencyId).push();
                databaseReference.setValue(attendance);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Attendance ")
                        .setMessage("Your Attendance have been marked!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })


                        .show();
            } else {
                Snackbar.make(coordinatorLayout, "Your are not in Office!", Snackbar.LENGTH_LONG).show();
            }

        } else {
            Snackbar.make(coordinatorLayout, "Your are not in Office!", Snackbar.LENGTH_LONG).show();
        }

    }
}
