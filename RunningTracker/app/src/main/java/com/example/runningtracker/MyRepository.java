package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository {
    //Initialising all values retrievable from Dao
    private ExerciseLogDao exerciseLogDao;
    private LiveData<List<ExerciseLog>> allLogs,logsByDistance, logsBySpeed, logsByTopSpeed, logsById, logsRunning, logsWalking;
    private LiveData<List<Float>> distanceByDate;

    MyRepository(Application application){
        //Retrieving room database using application context
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);

        //Setting all through the exerciseLogDao from the database
        exerciseLogDao = db.exerciseLogDao();
        allLogs = exerciseLogDao.getLogsAsc();
        logsByDistance = exerciseLogDao.getLogsDistanceDesc();
        logsBySpeed = exerciseLogDao.getLogsSpeedDesc();
        logsByTopSpeed = exerciseLogDao.getLogsTopSpeedDesc();
    }

    //Returning all logs in the table
    LiveData<List<ExerciseLog>> getAllLogs(){
        return allLogs;
    }

    //Returning logs ordered by distance
    LiveData<List<ExerciseLog>> getLogsByDistance(){
        return logsByDistance;
    }

    //Returning logs ordered by speed
    LiveData<List<ExerciseLog>> getLogsBySpeed(){
        return logsBySpeed;
    }

    //Returning all logs ordered by top speed
    LiveData<List<ExerciseLog>> getLogsByTopSpeed(){
        return logsByTopSpeed;
    }

    LiveData<List<ExerciseLog>> getLogsById(int id){
        logsById = exerciseLogDao.getLogsById(id);
        return logsById;
    }

    //Returning all logs at running speed
    LiveData<List<ExerciseLog>> getLogsRunning(){
        logsRunning = exerciseLogDao.getLogsRunning();
        return logsRunning;
    }

    //Returning all logs at walking speed
    LiveData<List<ExerciseLog>> getLogsWalking(){
        logsWalking = exerciseLogDao.getLogsWalking();
        return logsWalking;
    }

    //Returning all float distances for a certain date
    LiveData<List<Float>> getDistanceForDay(String someDate){
        distanceByDate = exerciseLogDao.getDistanceForDay(someDate);
        return distanceByDate;
    }

    //Returning all logs ordering them by date
    LiveData<List<ExerciseLog>> getLogsByDate(){
        return exerciseLogDao.getLogsByDate();
    }

    //Using the database and a lambda expression to insert a new log into the database
    void insert(ExerciseLog exerciseLog){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            exerciseLogDao.insert(exerciseLog);
        });
    }
}

