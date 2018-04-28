package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.R;

public class GoogleSignUp extends AppCompatActivity {
    private EditText Name, PhoneNo;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);
        btnSignUp=(Button)findViewById(R.id.SignUp);
        Name=(EditText)findViewById(R.id.signUpName);
        PhoneNo=(EditText)findViewById(R.id.signUpPhoneNo);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void onSignupClick(View view) {
        String name = Name.getText().toString().trim();
        String phone=PhoneNo.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;



        if (TextUtils.isEmpty(name)) {
            Name.setError(getString(R.string.error_field_required));
            focusView = Name;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            PhoneNo.setError(getString(R.string.error_field_required));
            focusView = PhoneNo;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
            return;
        }


        User user = new User(Name.getText().toString(), PhoneNo.getText().toString(), "", "");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        databaseReference.child("Users").child(firebaseUser.getUid()).setValue(user);
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("FCM_InstanceID").child(firebaseUser.getUid());
        dR.setValue(FirebaseInstanceId.getInstance().getToken());

        startActivity(new Intent(this, SubscriptonActivity.class));
        finish();


    }



}
