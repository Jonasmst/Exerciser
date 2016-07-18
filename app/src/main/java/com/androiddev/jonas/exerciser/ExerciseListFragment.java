package com.androiddev.jonas.exerciser;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 14.07.16.
 */
// TODO: List should be populated in onResume or something, rather than onCreate
public class ExerciseListFragment extends Fragment {

    // Views
    private FloatingActionButton newButton;
    private RecyclerView recyclerView;
    private ExerciseListAdapter adapter;
    private CoordinatorLayout mainLayout;
    private final String TAG = "ExerciseListFragment";

    // Variables
    ArrayList<ExerciseListItem> listItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_settings).setVisible(false);
        //menu.findItem(R.id.OptionsMenu_delete).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Exercises");
        SearchView searchView = new SearchView(ab.getThemedContext());
        //MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Search submit!");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Autocompletion
                String query = newText.toLowerCase();

                final ArrayList<ExerciseListItem> filteredList = new ArrayList<>();
                for(int i = 0; i < listItems.size(); i++) {
                    ExerciseListItem item = listItems.get(i);
                    final String text = item.getTitle().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(item);
                    }
                }

                // Update adapter's list
                adapter.setListData(filteredList);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        /** Check for messages **/
        String message = ((MainActivity)getActivity()).getFragmentTransactionMessage();
        if(message != null) {
            Snackbar snack = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT);
            snack.show();

            // Reset message in activity
            ((MainActivity)getActivity()).resetFragmentTransactionMessage();
        }

        /** Update list of exercises **/
        // Get exercises
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);
        ArrayList<Exercise> exercises = dbHandler.getAllExercises();
        listItems = new ArrayList<>();

        for(Exercise e : exercises) {
            ExerciseListItem item = new ExerciseListItem(e);
            listItems.add(item);

            // Log each item
            Log.d(TAG, "Name: " + e.getName());
            Log.d(TAG, "\tDesc: " + e.getDescription());
            Log.d(TAG, "\tSets: " + e.getSets());
            Log.d(TAG, "\tReps: " + e.getReps());
            Log.d(TAG, "\tQuan: " + e.getQuantity());
            Log.d(TAG, "\tMet :" + e.getMetric());
            Log.d(TAG, "");
        }

        // Set up adapter
        adapter = new ExerciseListAdapter(listItems, getActivity().getApplicationContext());
        adapter.setOnItemClickListener(new ExerciseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExerciseListItem item) {
                Log.d("ExerciseListFragment", "Adapter click callback");

                // Run new edit exercise fragment
                ((MainActivity) getActivity()).runEditExerciseFragment(item);
            }
        });

        recyclerView.setAdapter(adapter);

        // Set up onClick for new button
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).runNewExerciseFragment();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exercise_list_fragment,container,false);

        // Set up button
        newButton = (FloatingActionButton) v.findViewById(R.id.ExerciseListFragment_newButton);

        // Set up recycler view
        recyclerView = (RecyclerView) v.findViewById(R.id.ExerciseListFragment_recyclerView);

        // Set up main layout
        mainLayout = (CoordinatorLayout) v.findViewById(R.id.ExerciseListFragment_mainLayout);

        // Set up layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return v;
    }

}
