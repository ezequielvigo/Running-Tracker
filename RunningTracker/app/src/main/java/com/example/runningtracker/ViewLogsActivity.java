package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ViewLogsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ExerciseLogViewModel exerciseLogViewModel;
    private ExerciseLogAdapter adapter;
    private Spinner orderSpinner, exerciseSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        //Setting spinner objects to spinners from the layour
        orderSpinner = findViewById(R.id.orderSpinner);
        exerciseSpinner = findViewById(R.id.exerciseSpinner);

        //Retrieving string array from xml value file and setting it to the adapter then configuring adapter for drop down menu
        String[] orderOptions = getResources().getStringArray(R.array.order_options);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,orderOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Same as before for the other spinner object
        String[] exerciseOptions = getResources().getStringArray(R.array.exercise_options);
        ArrayAdapter exerciseSpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,exerciseOptions);
        exerciseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the adapters for the spinner objects
        exerciseSpinner.setAdapter(exerciseSpinnerAdapter);
        orderSpinner.setAdapter(spinnerAdapter);

        //Setting the item selected listeners for the spinners
        exerciseSpinner.setOnItemSelectedListener(this);
        orderSpinner.setOnItemSelectedListener(this);

        //Setting recyclerView object and creating adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new ExerciseLogAdapter(this);

        //Setting adapter and layout manager
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Setting the logViewModel
        exerciseLogViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ExerciseLogViewModel.class);

        //Retrieving all logs and setting adapter data to the LiveData compilation of them
        exerciseLogViewModel.getAllLogs().observe(this, exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all logs ordered by distance covered
    public void orderByDistance(){
        exerciseLogViewModel.getLogsByDistance().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all logs ordered by average speed
    public void orderBySpeed(){
        exerciseLogViewModel.getLogsBySpeed().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all logs ordered by top speed
    public void orderByTopSpeed(){
        exerciseLogViewModel.getLogsByTopSpeed().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all logs ordered by date
    public void orderByDate(){
        exerciseLogViewModel.getLogsByDate().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all running speed logs
    public void getRunning(){
        exerciseLogViewModel.getLogsRunning().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Sets adapter data to all walking speed logs
    public void getWalking(){
        exerciseLogViewModel.getLogsWalking().observe(this,exerciseLogs -> {
            adapter.setData(exerciseLogs);
        });
    }

    //Is called when an item is selected in a view
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Checking what view it is clicking on
        if (parent.getId() == R.id.orderSpinner) {
            String valueFromSpinner = parent.getItemAtPosition(position).toString();
            //Switch statement covering all options from the array
            switch(valueFromSpinner){
                case "Date":
                    orderByDate();
                    break;
                case "Average Speed":
                    orderBySpeed();
                    break;
                case "Top Speed":
                    orderByTopSpeed();
                    break;
                case "Distance Ran":
                    orderByDistance();
                case "Walking":
                    getWalking();
                    break;
                case "Running":
                    getRunning();
                    break;
            }
        } else if (parent.getId() == R.id.exerciseSpinner) {
            String valueFromSpinner = parent.getItemAtPosition(position).toString();
            switch(valueFromSpinner){
                case "Walking":
                    getWalking();
                    break;
                case "Running":
                    getRunning();
                    break;
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Exits the activity
    public void finish(View v){
        finish();
    }
}