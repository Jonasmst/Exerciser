package com.androiddev.jonas.exerciser;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonas on 15.07.16.
 */
public class ExerciseEditFragment extends Fragment {

    EditText nameEdit;
    EditText descEdit;
    EditText setsEdit;
    EditText repsEdit;
    EditText quantEdit;
    Spinner metricSpinner;
    FloatingActionButton editButton;
    CoordinatorLayout mainLayout;

    Exercise exercise;
    DatabaseHandler dbHandler;

    boolean spinnerCalled = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable delete button
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.OptionsMenu_delete).setVisible(true);

        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Edit exercise");

        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get ID from bundle
        int exerciseID = getArguments().getInt("ExerciseID");

        // Get exercise
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);
        exercise = dbHandler.getExerciseByID(exerciseID);

        View v = inflater.inflate(R.layout.exercise_edit_fragment, container, false);

        // Setup views
        mainLayout = (CoordinatorLayout) v.findViewById(R.id.ExerciseEditFragment_mainLayout);
        nameEdit = (EditText) v.findViewById(R.id.ExerciseEditFragment_name);
        descEdit = (EditText) v.findViewById(R.id.ExerciseEditFragment_description);
        setsEdit = (EditText) v.findViewById(R.id.ExerciseEditFragment_sets);
        repsEdit = (EditText) v.findViewById(R.id.ExerciseEditFragment_reps);
        quantEdit = (EditText) v.findViewById(R.id.ExerciseEditFragment_quant);
        metricSpinner = (Spinner) v.findViewById(R.id.ExerciseEditFragment_metric);

        // Set values from exercise to views
        // TODO: Validate that these values exist before setting them
        nameEdit.setText(exercise.getName());
        descEdit.setText(exercise.getDescription());
        setsEdit.setText(""+exercise.getSets());
        repsEdit.setText(""+exercise.getReps());
        quantEdit.setText(""+exercise.getQuantity());

        // Only update spinner if it's something other than 0
        // Otherwise, the item does not display correctly (bc of underline, I believe)
        if(metricToIndex(exercise.getMetric()) > 0) {
            metricSpinner.setSelection(metricToIndex(exercise.getMetric()));
        }

        // Set up button
        editButton = (FloatingActionButton) v.findViewById(R.id.ExerciseEditFragment_editButton);

        // Setup all onclicks
        fixOnClicks();

        return v;
    }

    // Setup textwatchers for edittexts and onclick for editButton
    private void fixOnClicks() {

        // Needed for automatic the adding of textwatchers
        ArrayList<EditText> fields = new ArrayList<>();
        fields.add(nameEdit);
        fields.add(descEdit);
        fields.add(setsEdit);
        fields.add(repsEdit);
        fields.add(quantEdit);

        for(EditText field : fields) {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(editButton.getVisibility() != View.VISIBLE) {
                        editButton.show();
                    }
                }
            });
        }

        // Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeChanges();
            }
        });

        // Metric spinner
        metricSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Show edit-button if it's not already visible
                if(spinnerCalled && editButton.getVisibility() != View.VISIBLE) {
                    Log.d("ExerciseEditFragment", "CALLED");
                    editButton.show();
                }

                // Flip switch
                if(!spinnerCalled) {
                    spinnerCalled = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Get values from views and store it in the database
    private void storeChanges() {
        // Get the values from all the views
        String newName = nameEdit.getText().toString();
        String newDesc = descEdit.getText().toString();
        int newSets = Integer.parseInt(setsEdit.getText().toString());
        int newReps = Integer.parseInt(repsEdit.getText().toString());
        int newQuant = Integer.parseInt(quantEdit.getText().toString());
        int metricIndex = metricSpinner.getSelectedItemPosition();
        String newMetric = indexToMetric(metricIndex);

        // Update exercise object
        exercise.setName(newName);
        exercise.setDescription(newDesc);
        exercise.setSets(newSets);
        exercise.setReps(newReps);
        exercise.setQuantity(newQuant);
        exercise.setMetric(newMetric);

        // Pass exercise-object to database handler update function
        dbHandler.editExercise(exercise);

        // Hide edit button again
        editButton.hide();

        // Show feedback
        Snackbar snack = Snackbar.make(mainLayout, "Changes saved", Snackbar.LENGTH_SHORT);
        snack.show();

        // Go back to previous fragment
        //getFragmentManager().popBackStack();

    }

    // Method for converting metrics to indexes
    private int metricToIndex(String metric) {
        if(metric.toLowerCase().equals("kilos")) {
            return 0;
        } else if (metric.toLowerCase().equals("kilometers")) {
            return 1;
        } else if (metric.toLowerCase().equals("minutes")) {
            return 2;
        }

        Log.e("ExerciseEditFragment", "metricToIndex returns -1. Input: " + metric);
        return -1;
    }

    // Method for converting indexes to metrics
    private String indexToMetric(int index) {
        if(index == 0) {
            return "Kilos";
        } else if(index == 1) {
            return "Kilometers";
        } else if(index == 2) {
            return "Minutes";
        }

        Log.e("ExerciseEditFragment", "indexToMetric returns null. Input: " + index);
        return null;
    }

    /** Dealing with the options-menu **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("ExerciseEditFragment", "Button clicked: " + item.getTitle());

        // Create onClick method for delete-button
        if(item.getItemId() == R.id.OptionsMenu_delete) {
            //dbHandler.deleteExercise(exercise.getId());
            Log.d("ExerciseEditFragment", "Deleting exercise: " + exercise.getName());

            // Create alertDialog confirming deletion
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete exercise?");
            builder.setMessage("Deleting \"" + exercise.getName() + "\" will remove related progress data.");

            String positiveText = "Delete";
            String negativeText = "Cancel";
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Remove exercise from database
                    dbHandler.deleteExercise(exercise.getId());

                    ((MainActivity)getActivity()).setFragmentTransactionMessage("Item deleted");

                    // Move to previous fragment
                    getFragmentManager().popBackStack();
                }
            });

            builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
