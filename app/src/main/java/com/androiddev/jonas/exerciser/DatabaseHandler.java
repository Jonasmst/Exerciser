package com.androiddev.jonas.exerciser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5; // Update everytime we do changes
    private static final String DATABASE_NAME = "exerciser.db";
    private static final String TAG = "DatabaseHandler";

    // Exercise stuff
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_ID = "_id";
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_EXERCISE_DESCRIPTION = "description";
    public static final String COLUMN_EXERCISE_SETS = "sets";
    public static final String COLUMN_EXERCISE_REPS = "reps";
    public static final String COLUMN_EXERCISE_QUANTITY = "quantity";
    public static final String COLUMN_EXERCISE_METRIC = "metric";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT, " +
                COLUMN_EXERCISE_DESCRIPTION + " TEXT, " +
                COLUMN_EXERCISE_SETS + " INTEGER, " +
                COLUMN_EXERCISE_REPS + " INTEGER, " +
                COLUMN_EXERCISE_QUANTITY + " INTEGER, " +
                COLUMN_EXERCISE_METRIC + " TEXT" +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        onCreate(db);
    }

    // Add a new exercise to the database
    public void addExercise(Exercise exercise) {

        // First, see if exercise with that name already exists.
        if(exerciseExists(exercise)) {
            Log.d(TAG, "addExercise() aborted - Exercise already exists.");
            return;
        }

        ContentValues values = new ContentValues();
        // Add attributes from object to DB entry
        values.put(COLUMN_EXERCISE_NAME, exercise.getName());
        values.put(COLUMN_EXERCISE_DESCRIPTION, exercise.getDescription());
        values.put(COLUMN_EXERCISE_SETS, exercise.getSets());
        values.put(COLUMN_EXERCISE_REPS, exercise.getReps());
        values.put(COLUMN_EXERCISE_QUANTITY, exercise.getQuantity());
        values.put(COLUMN_EXERCISE_METRIC, exercise.getMetric());

        // Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        // Insert entry (with values) into table
        db.insert(TABLE_EXERCISES, null, values);

        // Close database
        db.close();

        // Log
        Log.d("DBHandler", "Exercise added");
    }

    // Edit exercise
    public void editExercise(Exercise exercise) {
        // Get writable database object
        SQLiteDatabase db = getWritableDatabase();

        // Set new values
        ContentValues args = new ContentValues();
        args.put(COLUMN_EXERCISE_NAME, exercise.getName());
        args.put(COLUMN_EXERCISE_DESCRIPTION, exercise.getDescription());
        args.put(COLUMN_EXERCISE_SETS, exercise.getSets());
        args.put(COLUMN_EXERCISE_REPS, exercise.getReps());
        args.put(COLUMN_EXERCISE_QUANTITY, exercise.getQuantity());
        args.put(COLUMN_EXERCISE_METRIC, exercise.getMetric());

        // Update and close
        db.update(TABLE_EXERCISES, args, COLUMN_EXERCISE_ID + "=" + exercise.getId(), null);
        db.close();
    }

    // Delete exercise from database
    public void deleteExercise(int exerciseID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_ID + "=" + exerciseID + ";");
        db.close();
    }

    // Get exercise by exercise ID
    public Exercise getExerciseByID(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_ID + " = " + id + ";";
        Exercise exercise = null;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                int ex_id = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME));
                String desc = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_DESCRIPTION));
                int sets = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_SETS));
                int reps = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_REPS));
                int quant = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_QUANTITY));
                String metric = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_METRIC));

                exercise = new Exercise(name, desc, sets, reps, quant, metric);
                exercise.setId(ex_id);
            }
        } finally {
            db.close();
        }
        return exercise;
    }

    // Read all exercises from DB and return them as objects
    public ArrayList<Exercise> getAllExercises() {

        ArrayList<Exercise> objects = new ArrayList<Exercise>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISES + " ORDER BY LOWER(" + COLUMN_EXERCISE_NAME + ") ASC;";

        try {
            Cursor cursor = db.rawQuery(query, null);
            try {
                // Looping through all rows and adding to list
                if(cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
                        String name = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME));
                        String desc = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_DESCRIPTION));
                        int sets = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_SETS));
                        int reps = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_REPS));
                        int quant = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_QUANTITY));
                        String metric = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_METRIC));

                        Exercise ex = new Exercise(name, desc, sets, reps, quant, metric);
                        ex.setId(id);

                        objects.add(ex);
                    } while(cursor.moveToNext());
                }
            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }
        } finally {
            try {db.close(); } catch (Exception ignore) {}
        }

        return objects;
    }

    // Print out the database as a string (debug)
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISES;

        // Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_EXERCISE_NAME)) != null) {
                dbString += c.getString(c.getColumnIndex("name"));
                dbString += "\n";
                c.moveToNext();
            }
        }

        c.close();
        db.close();
        return dbString;
    }

    public boolean exerciseExists(Exercise exercise) {
        String name = exercise.getName();

        int count = -1;
        Cursor c = null;

        try {
            String query = "SELECT COUNT(*) FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_NAME + " = ?";
            SQLiteDatabase db = getReadableDatabase();
            c = db.rawQuery(query, new String[] {name});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            db.close();
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}
