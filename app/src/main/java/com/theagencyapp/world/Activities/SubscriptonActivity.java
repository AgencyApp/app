package com.theagencyapp.world.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.Employee;
import com.theagencyapp.world.R;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SubscriptonActivity extends AppCompatActivity
{
    EditText insertCode;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference agencyRefTable;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscriptonactivity);
        insertCode=(EditText)findViewById(R.id.suscribtion_insertCode);
        auth= FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
    }

    protected String authenticateCode(String code)
    {
        agencyRefTable= FirebaseDatabase.getInstance().getReference("AgencyRefTable/"+code);
        final String[] s = new String[1];
        agencyRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    s[0] =dataSnapshot.getValue(String.class);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return s[0];
    }

    public void onSubcriptionClick(View view){
        Intent intent= new Intent(this,AddCompanyActivity.class);
        startActivity(intent);
    }

    public void onInsertCodeEnter(View view)
    {
       final String code=insertCode.getText().toString();

        agencyRefTable= FirebaseDatabase.getInstance().getReference("AgencyRefTable/"+code);
        agencyRefTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                      String  agencyid =dataSnapshot.getValue(String.class);
                    if (!agencyid.equals(""))
                    {
                        FirebaseDatabase.getInstance().getReference("Users/"+firebaseUser.getUid()+"/agencyid").setValue(agencyid);
                        Intent intent = new Intent(SubscriptonActivity.this,MainActivity.class);
                        intent.putExtra("agencyId",agencyid);
                        if(code.startsWith("E"))
                        {
                            FirebaseDatabase.getInstance().getReference("Users/"+firebaseUser.getUid()+"/status").setValue("Employee");
                            FirebaseDatabase.getInstance().getReference("AgencyEmpRef/"+agencyid+"/").child(firebaseUser.getUid()).setValue(true);
                            ArrayList<String>temp=new ArrayList<>();
                            temp.add("Web");
                            temp.add("Android");
                            FirebaseDatabase.getInstance().getReference("Employees/"+firebaseUser.getUid()).setValue(new Employee(temp,"google.com"));
                            intent.putExtra("status","Employee");

                        }
                        else
                        {
                            FirebaseDatabase.getInstance().getReference("Users/"+firebaseUser.getUid()+"/status").setValue("Client");
                            FirebaseDatabase.getInstance().getReference("AgencyClientRef/"+agencyid+"/").child(firebaseUser.getUid()).setValue(true);
                            FirebaseDatabase.getInstance().getReference("Clients/"+firebaseUser.getUid()).setValue(new Client(5.0f,"google.com"));
                            intent.putExtra("status","Client");
                        }
                        sharedPref = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("agency_id", agencyid);
                        editor.commit();
                        startActivity(intent);
                        finish();
                    }


                }
                else
                {
                    insertCode.setError(getString(R.string.invalid_code));
                    insertCode.requestFocus();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
