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
import com.theagencyapp.world.R;
import com.theagencyapp.world.ClassModel.*;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, confirmInputPassword, Name, PhoneNo;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inputEmail=(EditText)findViewById(R.id.SignUpEmail);
        inputPassword=(EditText)findViewById(R.id.SignUpPassword);
        btnSignUp=(Button)findViewById(R.id.SignUp);
        confirmInputPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        Name=(EditText)findViewById(R.id.signUpName);
        PhoneNo=(EditText)findViewById(R.id.signUpPhoneNo);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void onSignupClick(View view)
    {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirm_password = inputPassword.getText().toString().trim();
        String name = Name.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_field_required));
            focusView = inputEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(name))
        {
            Name.setError(getString(R.string.error_field_required));
            focusView = Name;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_field_required));
            focusView = inputPassword;
            cancel = true;
        }

        if (password.length() < 6) {
            inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = inputPassword;
            cancel = true;
        }

        if (!password.equals(confirm_password))
        {
            confirmInputPassword.setError(getString(R.string.error_unequal_password));
            focusView = confirmInputPassword;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            User user=new User(Name.getText().toString(),PhoneNo.getText().toString(),"","");
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            databaseReference.child("Users").child(firebaseUser.getUid()).setValue(user);

                            startActivity(new Intent(SignUpActivity.this, SubscriptonActivity.class));
                            finish();
                        }
                    }
                });



    }
}
