package com.androiddev.jonas.exerciser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin on 04-06-2015.
 */
public class RoutineFragment extends Fragment {

    EditText nameField;
    EditText setsField;
    EditText repsField;
    EditText quantField;
    TextView dbResult;
    Button saveButton;
    DatabaseHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.routine_fragment,container,false);

        // Set up database handler
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);

        // Set up input fields
        nameField = (EditText) v.findViewById(R.id.RoutineFragment_exercise_name);
        setsField = (EditText) v.findViewById(R.id.RoutineFragment_number_sets);
        repsField = (EditText) v.findViewById(R.id.RoutineFragment_number_reps);
        quantField = (EditText) v.findViewById(R.id.RoutineFragment_number_weight);

        // Set up DB-text field
        dbResult = (TextView) v.findViewById(R.id.RoutineFragment_db_text);

        // Set up button
        saveButton = (Button) v.findViewById(R.id.RoutineFragment_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get string values from edittexts
                String e_name = nameField.getText().toString();
                String e_sets = setsField.getText().toString();
                String e_reps = repsField.getText().toString();
                String e_quant = quantField.getText().toString();

                // Check if any of the fields are left empty
                if (e_name.matches("") || e_sets.matches("") || e_reps.matches("") || e_quant.matches("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "A field is invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If all fields are populated, create new exercise and add to DB
                Exercise exercise = new Exercise(e_name, "Description", Integer.parseInt(e_sets), Integer.parseInt(e_reps), Integer.parseInt(e_quant), "kg");
                dbHandler.addExercise(exercise);

                // Reset input fields
                nameField.setText("");
                setsField.setText("");
                repsField.setText("");
                quantField.setText("");

                // Test writing out the database
                Log.d("RoutineFragment", "Printing DB");
                dbResult.setText(dbHandler.databaseToString());

            }
        });


        return v;
    }
}
