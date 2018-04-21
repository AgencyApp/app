package com.theagencyapp.world.Activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.ProjectFragment;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Activities.AddProject;
import com.theagencyapp.world.dummy.DummyContent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProjectFragment.OnListFragmentInteractionListener {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private SharedPreferences sharedPref;

    private String userName;
    private String userId;
    private String agencyId;
    private String agencyName;


    private DrawerLayout mDrawerLayout;


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
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDrawerLayout = findViewById(R.id.drawer_layout);

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
        loadProjectFragment();
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //agencyId = sharedPref.getString("agency_id", "");
        //userName = sharedPref.getString("user_name", "");
        //userId = sharedPref.getString("user_id", "");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        //TODO
                        if (id == R.id.nav_camera) {
                            // Handle the camera action
                        } else if (id == R.id.nav_gallery) {

                        } else if (id == R.id.nav_slideshow) {

                        } else if (id == R.id.nav_log_out) {
                            auth.signOut();
                        }


                        return true;
                    }
                });


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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem action) {
        if (action.content.equals("add_project")) {
            startActivity(new Intent(this, AddProject.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
