package com.androiddev.jonas.exerciser;

import java.util.ArrayList;

/**
 * Created by jonas on 14.07.16.
 */
public class Routine {

    String name;
    int _id;
    ArrayList<Exercise> exercises;

    public Routine(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    public void removeExercise(Exercise e) {
        exercises.remove(e);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

}
