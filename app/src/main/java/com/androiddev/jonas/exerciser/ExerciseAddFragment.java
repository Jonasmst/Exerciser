package com.androiddev.jonas.exerciser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by jonas on 18.07.16.
 */
public class ExerciseAddFragment extends Fragment {

    EditText nameField;
    EditText descField;
    EditText setsField;
    EditText repsField;
    EditText quantField;
    Spinner metricSpinner;
    FloatingActionButton addButton;

    DatabaseHandler dbHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Need this to hide the options menu, ironically
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Hide options menu
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.OptionsMenu_delete).setVisible(false);

        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Add new exercise");

        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.exercise_add_fragment, container, false);

        // Setup database
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);

        // Setup views
        nameField = (EditText) v.findViewById(R.id.ExerciseAddFragment_name);
        descField = (EditText) v.findViewById(R.id.ExerciseAddFragment_description);
        setsField = (EditText) v.findViewById(R.id.ExerciseAddFragment_sets);
        repsField = (EditText) v.findViewById(R.id.ExerciseAddFragment_reps);
        quantField = (EditText) v.findViewById(R.id.ExerciseAddFragment_quant);
        metricSpinner = (Spinner) v.findViewById(R.id.ExerciseAddFragment_metric);
        addButton = (FloatingActionButton) v.findViewById(R.id.ExerciseAddFragment_addButton);

        // Setup all onClicks
        fixOnClicks();

        return v;
    }

    public void fixOnClicks() {
        // Add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from all the views
                // TODO: Validate that no views are empty
                // TODO: Remove newlines from string values
                // TODO: Validate that there's no existing exercise with the same name
                String name = nameField.getText().toString();
                String desc = descField.getText().toString();
                int sets = Integer.parseInt(setsField.getText().toString());
                int reps = Integer.parseInt(repsField.getText().toString());
                int quant = Integer.parseInt(repsField.getText().toString());
                String metric = indexToMetric(metricSpinner.getSelectedItemPosition());

                Exercise exercise = new Exercise(name, desc, sets, reps, quant, metric);
                dbHandler.addExercise(exercise);

                // TODO: Maybe validate if this actually goes through, before saying it did
                // Send message to be displayed as snackbar
                ((MainActivity)getActivity()).setFragmentTransactionMessage("Exercise added");

                // Move to previous fragment (list of exercises)
                getFragmentManager().popBackStack();
            }
        });

        // Needed for automatic the adding of textwatchers
        ArrayList<EditText> fields = new ArrayList<>();
        fields.add(nameField);
        fields.add(descField);
        fields.add(setsField);
        fields.add(repsField);
        fields.add(quantField);

        // Make addButton appear when a field is populated
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
                    if(addButton.getVisibility() != View.VISIBLE) {
                        addButton.show();
                    }
                }
            });
        }
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
}
