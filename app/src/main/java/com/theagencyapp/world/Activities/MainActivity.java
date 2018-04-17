package com.theagencyapp.world.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theagencyapp.world.ProjectFragment;
import com.theagencyapp.world.R;
import com.theagencyapp.world.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ProjectFragment.OnListFragmentInteractionListener {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_projects:
                    loadProjectFragment();
                    return true;
                case R.id.navigation_teams:
                    //mTextMessage.setText(R.string.title_teams);
                    return true;
                case R.id.navigation_clients:
                    //mTextMessage.setText(R.string.title_clients);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        auth=FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        if (savedInstanceState == null) {
            loadProjectFragment();
        }


    }

    public void onLogout(View v)
    {
        auth.signOut();

        // this listener will be called when there is change in firebase user session

    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authListener);
    }

    private void loadProjectFragment() {

        Fragment fragment = ProjectFragment.newInstance(0);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
