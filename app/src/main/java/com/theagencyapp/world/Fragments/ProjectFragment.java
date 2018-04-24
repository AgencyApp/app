package com.theagencyapp.world.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.theagencyapp.world.Adapters.MyProjectRecyclerViewAdapter;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.Interfaces.OnListFragmentInteractionListener;
import com.theagencyapp.world.R;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProjectFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    Context c;
    private ArrayList<Project> projects;
    private ProgressBar projectsLoading;
    private MyProjectRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectFragment() {
    }


    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        view.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        projects = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //projects.add(new Project("Web Dev", "2", "aaa", "aaa", "aaa", "high"));
        //projects.add(new Project("Photographire", "2", "aaa", "aaa", "aaa", "medium"));

        recyclerView = view.findViewById(R.id.projects_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new MyProjectRecyclerViewAdapter(projects, mListener));

        getProject();


        projectsLoading = view.findViewById(R.id.progressBarProjects);




        FloatingActionButton myFab = view.findViewById(R.id.add_project_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onListFragmentInteraction(null, "AddProject", true);
            }
        });



       /* View mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_project_list, null);
        FrameLayout fl = (FrameLayout) mRootView.findViewById(R.id.project_layout);
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return mRootView;*/


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



    void getProject()
    {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        String agencyId=sharedPreferences.getString("agency_id","h");
        DatabaseReference projectRef=firebaseDatabase.getReference("ProjectRefTable/"+agencyId);
        projectRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prevChildKey) {
                fetchProjectData(snapshot.getKey());
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

    void fetchProjectData(String projectId)
    {
        DatabaseReference databaseReference=firebaseDatabase.getReference("Projects/"+projectId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project project=dataSnapshot.getValue(Project.class);
                projects.add(project);
                projectsLoading.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
