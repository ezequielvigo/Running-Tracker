package com.example.runningtracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseLogViewModel extends AndroidViewModel {
    private MyRepository myRepository;
    private final LiveData<List<ExerciseLog>> allLogs;

    //Initialising constructor, setting repository to myRepository using application context
    public ExerciseLogViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
        allLogs = myRepository.getAllLogs();
    }

    //Using the repository to get all logs
    LiveData<List<ExerciseLog>> getAllLogs(){
        return allLogs;
    }

    LiveData<List<ExerciseLog>> getLogsByDistance(){return myRepository.getLogsByDistance();}

    LiveData<List<ExerciseLog>> getLogsBySpeed(){ return myRepository.getLogsBySpeed();}

    LiveData<List<ExerciseLog>> getLogsByTopSpeed(){ return myRepository.getLogsByTopSpeed();}

    LiveData<List<ExerciseLog>> getLogsByDate(){return myRepository.getLogsByDate();}

    LiveData<List<ExerciseLog>> getLogsRunning(){return myRepository.getLogsRunning();}

    LiveData<List<ExerciseLog>> getLogsWalking(){return myRepository.getLogsWalking();}



    //Using the repository methods to insert a new exercise log
    public void insert(ExerciseLog exerciseLog){
        myRepository.insert(exerciseLog);
    }
}
