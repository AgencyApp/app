package com.theagencyapp.world.Activities;

import android.content.Intent;
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
import com.theagencyapp.world.R;

import java.util.concurrent.Semaphore;

public class SubscriptonActivity extends AppCompatActivity
{
    EditText insertCode;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference agencyRefTable;

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
                            FirebaseDatabase.getInstance().getReference("AgencyEmpRef/"+agencyid+"/").setValue(firebaseUser.getUid());
                            intent.putExtra("status","Employee");

                        }
                        else
                        {
                            FirebaseDatabase.getInstance().getReference("Users/"+firebaseUser.getUid()+"/status").setValue("Client");
                            FirebaseDatabase.getInstance().getReference("AgencyClientRef/"+agencyid+"/").setValue(firebaseUser.getUid());
                            intent.putExtra("status","Client");
                        }
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
