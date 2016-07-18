package com.androiddev.jonas.exerciser;

/**
 * Created by jonas on 14.07.16.
 */
public class Exercise {

    // Exercise attributes
    private String name;
    private String description;
    private int _id;
    private int sets;
    private int reps;
    private int quantity; // Number of kg or min
    private String metric; // Either kg or min

    /** Constructor **/
    public Exercise(String name, String description, int sets, int reps, int quantity, String metric) {
        this.name = name;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
        this.quantity = quantity;
        this.metric = metric;
    }

    /** Getters and setters **/
    public String getName() {
        return this.name;
    }

    public String getDescription() { return this.description; }

    public int getId() {
        return this._id;
    }

    public int getSets() {
        return this.sets;
    }

    public int getReps() {
        return this.reps;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getMetric() {
        return this.metric;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) { this.description = desc; }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setId(int id) {
        this._id = id;
    }

}
