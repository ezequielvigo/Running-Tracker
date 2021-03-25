package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_table")

public class ExerciseLog {
    //Primary key is the id which is unique and has an autoincrement
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    //Rest of the columns with their respective names
    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name = "distance")
    public float distance;

    @ColumnInfo(name = "time")
    public int time;

    @ColumnInfo(name = "averageSpeed")
    public float speed;

    @ColumnInfo(name = "topSpeed")
    public float topSpeed;

    //Constructor for the exerciselog entity
    public ExerciseLog(float distance, int time, String date, float speed, float topSpeed) {
        //Setting all the values
        this.distance = distance;
        this.time = time;
        this.date = date;
        this.speed = speed;
        this.topSpeed = topSpeed;
    }
    //Below are all the getter methods for the values

    public int getTime(){
        return this.time;
    }

    public float getDistance(){
        return this.distance;
    }

    public float getSpeed(){
        return this.speed;
    }

    public float getTopSpeed(){
        return this.topSpeed;
    }

    public String getDate(){
        return this.date;
    }
}
