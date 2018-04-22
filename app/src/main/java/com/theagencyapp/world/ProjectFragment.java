package com.theagencyapp.world;

import android.content.Context;
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

import com.theagencyapp.world.ClassModel.Project;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProjectFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
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


}
