package com.theagencyapp.world.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.theagencyapp.world.ClassModel.MyLocation;
import com.theagencyapp.world.Fragments.ClientFragment;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.Fragments.ProjectFragment;
import com.theagencyapp.world.R;
import com.theagencyapp.world.Fragments.TeamFragment;
import com.theagencyapp.world.Services.SinchService;
import com.theagencyapp.world.Utility.ProfilePicture;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends BaseActivity implements OnListFragmentInteractionListener, SinchService.StartFailedListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private SharedPreferences sharedPref;

    private String userName;
    private String userId;
    private String agencyId;
    private String agencyName;
    private boolean callClicked;

    private FirebaseAnalytics mFirebaseAnalytics;

    ImageView dp;

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
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        agencyId = sharedPreferences.getString("agency_id", "h");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        callClicked = false;
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

        //  SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "h");

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.person_name)).setText(name);

        dp = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        ProfilePicture.setProfilePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(), dp);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_camera) {
                            startActivity(new Intent(MainActivity.this, EmployeeProfile.class));
                        } else if (id == R.id.nav_tweets) {
                            GetTweets getTweets = new GetTweets();
                            getTweets.execute();
                        } else if (id == R.id.nav_log_out) {
                            auth.signOut();
                        } else if (id == R.id.nav_attendance) {

                            startActivity(new Intent(MainActivity.this, AttendanceLog.class));
                        } else if (id == R.id.nav_agencyLocation) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivityForResult(intent, 415);

                        } else if (id == R.id.nav_firebase) {
                            onInviteClick();
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
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, "ServiceFalied", Toast.LENGTH_LONG);
    }


    @Override
    public void onStarted() {
        if (callClicked) {
            callClicked = false;
            openPlaceCallActivity();
        }
    }

    public class GetTweets extends AsyncTask<Void, Void, Void> {
        private Twitter mtwitter;
        List<twitter4j.Status> tweets;

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("81hnFl5b4VM3hUqaApWlZt9eU")
                        .setOAuthConsumerSecret("MqWwpdEbryt3gyf4AbQGVlGJ4U8SGNXJJ5X7nYYBTaCNRbPQnB")
                        .setOAuthAccessToken("988011971223216128-RlnnJWjgqVzYXo0NA9T2kCR29AEo7oQ")
                        .setOAuthAccessTokenSecret("Ob28nHSnq3lA2zSD7tIWmrmiGAGSAEzHMK3VBVh6GhuQ8");
                TwitterFactory tf = new TwitterFactory(cb.build());
                mtwitter = tf.getInstance();
                tweets = mtwitter.getHomeTimeline();
                publishProgress();
                //notify data set change.
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setTitle("Tweets");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
            for (twitter4j.Status tweet : tweets) {
                arrayAdapter.add(tweet.getText());
            }

            builderSingle.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builderSingle.show();
        }
    }

    public void onInviteClick() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Agency")
                .setMessage("Download my app!")
                .setDeepLink(Uri.parse("https://agency.com"))
                .setCallToActionText("Send")
                .build();
        startActivityForResult(intent, 2);
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
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Project");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, bundle.getString("project_name"));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent = new Intent(this, ProjectDetailsActivity.class);
                intent.putExtra("details", details);
                startActivity(intent);
            } else if (action.equals("TeamDetails")) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Team");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, bundle.getString("team_id"));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent = new Intent(this, TeamDetailsActivity.class);
                intent.putExtra("details", details);
                startActivity(intent);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1)
                ProfilePicture.setProfilePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(), dp);
        } else if (requestCode == 415 && data != null) {
            MyLocation myLocation = new MyLocation(data.getDoubleExtra("lng", 0), data.getDoubleExtra("lat", 0));
            FirebaseDatabase.getInstance().getReference("AgencyLocation").child(agencyId).setValue(myLocation);
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
            case R.id.Call_icon:
                callClicked();
                return true;
            case R.id.markAttendence:
                startActivity(new Intent(this, AttendanceSystem.class));
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

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(MainActivity.this);
    }
    private void callClicked() {
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        callClicked = true;

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!userName.equals(getSinchServiceInterface().getUserName())) {
            getSinchServiceInterface().stopClient();
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            // showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, AllUsersForCall.class);
        startActivity(mainActivity);
    }



}
