package com.example.runningtracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseLogDao {
    //Used to insert new log into the table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ExerciseLog exerciseLog);

    //Deletes all entries from the table
    @Query("DELETE FROM exercise_table")
    void deleteAll();

    //Gets all the logs ordered by id ascending
    @Query("SELECT * FROM exercise_table ORDER BY id ASC")
    LiveData<List<ExerciseLog>> getLogsAsc();

    //Returns all the distance ran values for a single date
    @Query("SELECT distance FROM exercise_table WHERE date LIKE :someDate")
    LiveData<List<Float>>getDistanceForDay(String someDate);

    //Returns all of the logs for a given id which should only be one
    @Query("SELECT * FROM exercise_table WHERE id LIKE :someId")
    LiveData<List<ExerciseLog>>getLogsById(int someId);

    //Gets all the logs ordered by the distance ran descending
    @Query("SELECT * FROM exercise_table ORDER BY distance DESC")
    LiveData<List<ExerciseLog>>getLogsDistanceDesc();

    //Gets all the logs ordered by the speed descending
    @Query("SELECT * FROM exercise_table ORDER BY averageSpeed DESC")
    LiveData<List<ExerciseLog>>getLogsSpeedDesc();

    //Gets all the logs ordered by the top speed descending
    @Query("SELECT * FROM exercise_table ORDER BY topSpeed DESC")
    LiveData<List<ExerciseLog>>getLogsTopSpeedDesc();

    //Gets all the logs that are faster than walking speed
    @Query("SELECT * FROM exercise_table WHERE averageSpeed >= 7.2")
    LiveData<List<ExerciseLog>>getLogsRunning();

    //Gets all the logs that are slower than running speed
    @Query("SELECT * FROM exercise_table WHERE averageSpeed < 7.2")
    LiveData<List<ExerciseLog>>getLogsWalking();

    //Retrieves logs ordered by date ascending
    @Query("SELECT * FROM exercise_table ORDER BY date ASC")
    LiveData<List<ExerciseLog>>getLogsByDate();

}
