package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theagencyapp.world.R;

public class SubscriptonActivity extends AppCompatActivity
{
    EditText insertCode;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscriptonactivity);
        insertCode=(EditText)findViewById(R.id.suscribtion_insertCode);
        auth= FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
    }

    protected boolean authenticateCode(String code)
    {
        return true;
    }

    public void onSubcriptionClick(View view){
        Intent intent= new Intent(this,AddCompanyActivity.class);
        startActivity(intent);
    }

    public void onInsertCodeEnter(View view)
    {
       String code=insertCode.getText().toString();
        if (authenticateCode(code))
        {

        }
        else
        {
            insertCode.setError(getString(R.string.invalid_code));
            insertCode.requestFocus();
            return;

        }


    }
}
