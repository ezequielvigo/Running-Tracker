package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Navigation to the view logs activity
    public void viewLogs(View v){
        Intent intent = new Intent(this, ViewLogsActivity.class);
        startActivity(intent);
    }

    //Navigation to the exercise logging activity
    public void logExercise(View v){
        Intent intent = new Intent(this, LogExerciseActivity.class);
        startActivity(intent);
    }
}