package com.androiddev.jonas.exerciser;

/**
 * Created by jonas on 14.07.16.
 */
public class ExerciseListItem {

    private String title;
    private String subTitle;
    private int id;
    //private int imageResID;

    public ExerciseListItem(Exercise exercise) {
        this.title = exercise.getName();
        this.subTitle = exercise.getDescription();
        this.id = exercise.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() { return subTitle; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String sub) { this.subTitle = sub; }

    public int getExerciseId() {
        return this.id;
    }

}
