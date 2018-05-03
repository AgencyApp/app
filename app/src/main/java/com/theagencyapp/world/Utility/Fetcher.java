package com.theagencyapp.world.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.Client_Display;
import com.theagencyapp.world.ClassModel.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hamza on 18-Apr-18.
 */

public class Fetcher {

    ArrayList<Client_Display> client_displays;

    public void FetchClient() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference agid = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid = dataSnapshot.getValue(String.class);
                DatabaseReference clients = firebaseDatabase.getReference("AgencyClientRef/" + agencyid);
                clients.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue(boolean.class)) {
                                fetchClientData(snapshot.getKey());
                            }
                        }
                        //onDatasetnotify();
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

    public void fetchClientData(final String clientId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference user = firebaseDatabase.getReference("Users/" + clientId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                DatabaseReference client = firebaseDatabase.getReference("Client/" + clientId);
                client.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Client client = dataSnapshot.getValue(Client.class);
                        // client_displays.add(new Client_Display(user.getName(),user.getPhoneNo(),user.getAgencyid(),user.getStatus(),client.getRatings(),client.getImageUrl()));
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
}
