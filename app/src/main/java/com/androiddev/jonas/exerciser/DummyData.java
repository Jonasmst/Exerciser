package com.androiddev.jonas.exerciser;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jonas on 14.07.16.
 */
public class DummyData {

    public DummyData(Context c) {

        createDummyData(c);
    }

    public void createDummyData(Context c) {

        String[] exercises = new String[] {
                "Bicep curls 21",
                "Hip thrusts",
                "Benkpress",
                "Skuldre utover",
                "Skuldre oppover",
                "Rygghev",
                "Båt",
                "Hjulet",
                "Jogging",
                "Bryst manualer, flatbenk",
                "Bryst manualer, skråbenk",
                "Roing flatbenk",
                "Roing maskin",
                "Rosmaskin",
                "Nedtrekk",
                "Benhev",
                "Knebøy",
                "Utfall, vanrende",
                "Triceps",
                "Biceps curls, stativ",
                "Markløft",
                "Hangups",
                "Chin-ups",
                "Dips, foran",
                "Dips, bak",
                "Benpress, maskin",
                "Legger, maskin",
                "Flys, bryst",
                "Flys, rygg, maskin"
        };

        Random rand = new Random();
        DatabaseHandler dbHandler = new DatabaseHandler(c, null, null, 1);

        for(String ex : exercises) {

            Exercise exercise = new Exercise(ex, "This is medium long textual description of this entry." , rand.nextInt(5), rand.nextInt(15), rand.nextInt(75), "kilos");
            dbHandler.addExercise(exercise);
        }

    }

    public void removeAllExercises(Context c) {
        DatabaseHandler dbHandler = new DatabaseHandler(c, null, null, 1);
        ArrayList<Exercise> exs = dbHandler.getAllExercises();

        for(Exercise e : exs) {
            dbHandler.deleteExercise(e.getId());
        }
    }

}
