package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theagencyapp.world.Fragments.ClientFragment;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.Fragments.ProjectFragment;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Fragments.TeamFragment;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
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
                    loadTeamFragment();
                    return true;
                case R.id.navigation_clients:
                    loadClientFragment();
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

        //SharedPreferences sharedPreferences=this.getSharedPreferences("data", Context.MODE_PRIVATE);
        //String name=sharedPreferences.getString("name","h");

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

        Fragment fragment = ProjectFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
    }

    private void loadTeamFragment() {

        Fragment fragment = TeamFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
    }

    private void loadClientFragment() {

        Fragment fragment = ClientFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
    }


    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        if (isFabClicked) {
            if (action.equals("AddProject"))
                startActivity(new Intent(this, AddProject.class));
            else if (action.equals("AddTeam"))
                startActivity(new Intent(this, AddTeam.class));
        } else {
            if (action.equals("ProjectDetails")) {
                Intent intent = new Intent(this, ProjectDetailsActivity.class);
                intent.putExtra("details", details);
                startActivity(intent);
            } else if (action.equals("TeamDetails")) {
                Intent intent = new Intent(this, TeamDetailsActivity.class);
                intent.putExtra("details", details);
                startActivity(intent);
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.chat_icon:
                startActivity(new Intent(this, Chat.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }


}
