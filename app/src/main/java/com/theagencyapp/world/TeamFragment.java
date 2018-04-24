package com.theagencyapp.world;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Team;
import com.theagencyapp.world.ClassModel.Team_Display;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TeamFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Team_Display> teams;
    private TeamRecyclerViewAdapter adapter;
    private ProgressBar teamsLoading;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamFragment() {
    }


    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.teams_list);
        teams = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new TeamRecyclerViewAdapter(teams, mListener));
        teamsLoading = view.findViewById(R.id.progressBarTeams);
        FetchTeams();

        FloatingActionButton myFab = view.findViewById(R.id.add_team_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onListFragmentInteraction(null, "AddTeam", true);
            }
        });

        return view;
    }

    private void FetchTeams() {

        DatabaseReference agid = firebaseDatabase.getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/agencyid");
        agid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agencyid = dataSnapshot.getValue(String.class);
                final DatabaseReference clients = firebaseDatabase.getReference("AgencyTeamRef/" + agencyid);
                clients.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String prevChildKey) {
                        fetchTeamData(snapshot.getKey());
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchTeamData(final String teamId) {
        DatabaseReference user = firebaseDatabase.getReference("Teams/" + teamId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                teams.add(new Team_Display(team.getName(), team.getEmployeeId(), dataSnapshot.getKey()));
                teamsLoading.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


}
