package com.theagencyapp.world;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theagencyapp.world.ClassModel.Project;
import com.theagencyapp.world.dummy.DummyContent;
import com.theagencyapp.world.dummy.DummyContent.DummyItem;
import com.theagencyapp.world.Activities.AddProject;
import com.theagencyapp.world.ClassModel.Project;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

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
        Log.d("a", Integer.toString(container.getHeight()));
        view.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        projects = new ArrayList<>();
        projects.add(new Project("Web Dev", "2", "aaa", "aaa", "aaa", "high"));
        projects.add(new Project("Photographire", "2", "aaa", "aaa", "aaa", "medium"));

        recyclerView = view.findViewById(R.id.projects_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyProjectRecyclerViewAdapter(projects, mListener));


        projectsLoading = view.findViewById(R.id.progressBarProjects);

        projectsLoading.setVisibility(View.GONE);


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem action);
    }

    void getProject()
    {
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        String agencyId=sharedPreferences.getString("agency_id","h");
        DatabaseReference projectRef=firebaseDatabase.getReference("ProjectRefTable/"+agencyId);
        projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(boolean.class))
                    {
                        fetchProjectData(snapshot.getKey());
                    }
                }

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
