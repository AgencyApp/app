package com.theagencyapp.world.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.MileStone;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.Adapters.MilestonesRecyclerViewAdapter;
import com.theagencyapp.world.R;

import java.util.ArrayList;

public class ProjectDetailsActivity extends AppCompatActivity {


    ArrayList<MileStone> milestones;
    FirebaseDatabase firebaseDatabase;
    ImageView clientIcon;
    TextView clientName;
    MilestonesRecyclerViewAdapter adapter;
    String milestonesContainer;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private InterstitialAd mInterstitialAd;
    private TextView mLevelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        Intent intent = getIntent();
        Bundle details = intent.getBundleExtra("details");
        String projectName = details.getString("project_name");
        milestonesContainer = details.getString("milestones_container_id");
        String teamId = details.getString("team_id");
        String clientId = details.getString("client_id");
        String description = details.getString("description");
        String deadline = details.getString("deadline");
        String priority = details.getString("priority");

        firebaseDatabase = FirebaseDatabase.getInstance();
        ((TextView) findViewById(R.id.project_description)).setText(description);
        ((TextView) findViewById(R.id.project_deadline)).setText(deadline);
        ((TextView) findViewById(R.id.project_priority_text)).setText(priority.substring(0, 1).toUpperCase() + priority.substring(1));
        ImageView priorityIcon = findViewById(R.id.project_priority_icon);
        clientIcon = findViewById(R.id.project_client_icon);
        clientName = ((TextView) findViewById(R.id.project_client_name));

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        int id = R.drawable.fire;
        switch (priority) {
            case "high":
                id = R.drawable.fire;
                break;
            case "medium":
                id = R.drawable.drop;
                break;
            case "low":
                id = R.drawable.leaf;
                break;

        }

        ((TextView) findViewById(R.id.project_deadline)).setText(deadline);
        priorityIcon.setImageDrawable(ContextCompat.getDrawable(this, id));
        setTitle(projectName);

        if (clientId == null) {
            clientIcon.setVisibility(View.GONE);
            clientName.setText(R.string.no_client);
        } else {
            fetchClientData(clientId);
        }

        if (milestonesContainer != null)
            fetchMilestoneList(milestonesContainer);

        milestones = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.milestones_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new MilestonesRecyclerViewAdapter(milestones));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
                finish();
            }
        });


    }

    public void shareToFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_icon:
                shareToFacebook();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void fetchMilestoneList(String milestoneContainerId) {

        DatabaseReference container = firebaseDatabase.getReference("MilestoneContainer/" + milestoneContainerId);
        container.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prevChildKey) {
                fetchMilestone(snapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


    }

    void fetchMilestone(String milestoneId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Milestones/" + milestoneId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                milestones.add(dataSnapshot.getValue(MileStone.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onTeamViewClick(View view) {

    }

    public void onAddMilestoneClick(View view) {
        Intent intent = new Intent(this, AddMilestone.class);
        intent.putExtra("containerId", milestonesContainer);
        startActivity(intent);
    }

    private void fetchClientData(final String clientId) {
        DatabaseReference user = firebaseDatabase.getReference("Users/" + clientId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                final DataSnapshot temp = dataSnapshot;
                DatabaseReference client = firebaseDatabase.getReference("Clients/" + clientId);
                client.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Client client = dataSnapshot.getValue(Client.class);
                        //TODO: Set Client Image
                        //if(client!=null)
                        //client.getImageUrl();
                        //clientIcon.setImage(url)
                        //clientName.setText(name)
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {

            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }
}
