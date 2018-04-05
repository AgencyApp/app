package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.R;
import com.theagencyapp.world.ClassModel.*;

public class AddCompanyActivity extends AppCompatActivity {
    EditText companyName;
    EditText address;
    EditText companyType;
    EditText logoUrl;
    private FirebaseAuth auth;
    private DatabaseReference insertCodeReference;
    private DatabaseReference companyRefrence;
    int currentInsertCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agency);
        companyName=(EditText)findViewById(R.id.addCompany_name);
        address=(EditText)findViewById(R.id.addCompany_Address);
        companyType=(EditText)findViewById(R.id.addCompany_type);
        logoUrl=(EditText)findViewById(R.id.addCompany_logoUrl);
        insertCodeReference= FirebaseDatabase.getInstance().getReference("meta_data/insert_code");
        companyRefrence=FirebaseDatabase.getInstance().getReference();
        insertCodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s=dataSnapshot.getValue(String.class);
                currentInsertCode=Integer.parseInt(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onAddCompanyClick(View view)
    {
        String company_name = companyName.getText().toString().trim();
        String logo = logoUrl.getText().toString().trim();
        String company_address = address.getText().toString().trim();
        String type = companyType.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(company_name))
        {
            companyName.setError(getString(R.string.error_field_required));
            focusView = companyName;
            cancel = true;
        }

        if (TextUtils.isEmpty(logo))
        {
            logoUrl.setError(getString(R.string.error_field_required));
            focusView = logoUrl;
            cancel = true;
        }

        if (TextUtils.isEmpty(company_address))
        {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }

        if (TextUtils.isEmpty(type))
        {
            companyType.setError(getString(R.string.error_field_required));
            focusView = companyType;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
            return;
        }


        Company company=new Company(companyName.getText().toString(),logoUrl.getText().toString(),address.getText().toString(),companyType.getText().toString(),currentInsertCode+1,currentInsertCode+2);
        companyRefrence.child("Companies").push().setValue(company);
        insertCodeReference.setValue(currentInsertCode+2);
        Toast.makeText(this,"done",Toast.LENGTH_LONG);
    }



}
