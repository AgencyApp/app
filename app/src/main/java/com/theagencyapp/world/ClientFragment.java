package com.theagencyapp.world;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Client;
import com.theagencyapp.world.ClassModel.Client_Display;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.ClassModel.User;
import com.theagencyapp.world.Utility.Logger;

import java.util.ArrayList;

/**
 * Created by abdul on 4/23/2018.
 */

public class ClientFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    Context c;
    private ArrayList<Client_Display> clients;
    private ProgressBar clientsLoading;
    private ClientsRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClientFragment() {
    }


    public static ClientFragment newInstance() {
        return new ClientFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        view.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        clients = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.clients_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new ClientsRecyclerViewAdapter(clients));

        getClients();

        clientsLoading = view.findViewById(R.id.progressBarClients);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    void getClients() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String agencyId = sharedPreferences.getString("agency_id", "h");
        DatabaseReference clientRef = firebaseDatabase.getReference("AgencyClientRef/" + agencyId);
        clientRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prevChildKey) {
                if (snapshot.getValue(boolean.class)) {
                    fetchClientData(snapshot.getKey());
                }
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
                        if (client != null) {
                            clients.add(new Client_Display(user.getName(), user.getPhoneNo(), user.getAgencyid(), user.getStatus(), client.getRatings(), temp.getKey(), client.getImageUrl()));
                            clientsLoading.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.logError("Client Fragment", null, "Error fetching client data");
            }
        });
    }

}
