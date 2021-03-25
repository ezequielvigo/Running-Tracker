package com.example.runningtracker;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities={ExerciseLog.class}, version = 2, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    //The dao used for my exercise log
    public abstract ExerciseLogDao exerciseLogDao();

    //The different values needed for the database
    private static volatile MyRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Creating the database
    static MyRoomDatabase getDatabase(final Context context){
        //If not yet created building it assigning it the name exercise_database
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MyRoomDatabase.class, "exercise_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(createCallback).build();
        }
        return INSTANCE;
    }

    // Creating a callback allowing me to introduce methods into onCreate
    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d("g53mdp", "dboncreate");
            databaseWriteExecutor.execute(() -> {
                //Upon creation all elements are deleted from the database
                ExerciseLogDao exerciseLogDao = INSTANCE.exerciseLogDao();
                exerciseLogDao.deleteAll();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat();
                String date = sdf.format(calendar.getTime());

                ExerciseLog exerciseLog = new ExerciseLog(10,30,date, 5.5f, (float) 6.7);
                exerciseLogDao.insert(exerciseLog);

            });
        }
    };

}

