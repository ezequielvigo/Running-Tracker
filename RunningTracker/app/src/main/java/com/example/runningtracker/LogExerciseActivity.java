package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.round;

public class LogExerciseActivity extends AppCompatActivity {
    //private LogService.MyBinder myService = null;
    private BroadcastReceiver broadcastReceiver;
    private MyRepository myRepository = null;
    TextView distanceRanView, nowTracking, speedView, finishTracking;
    public float distanceRan = 0, topSpeed = 0, speed = 0;
    public long initialTime = 0, endTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_exercise);

        myRepository = new MyRepository(getApplication());

        distanceRanView = findViewById(R.id.currentDistanceRanView);
        speedView = findViewById(R.id.currentSpeedView);
        nowTracking = findViewById(R.id.nowTracking);
        finishTracking = findViewById(R.id.finishTracking);

        nowTracking.setVisibility(View.INVISIBLE);
        finishTracking.setVisibility(View.INVISIBLE);

        if(!runtimePermissions()){
            enableButtons();
        };
    }

    public boolean runtimePermissions(){
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    //Enables the buttons to be used
    private void enableButtons(){
        Button startTrackingButton = findViewById(R.id.beginTrackingButton);
        Button stopTrackingButton = findViewById(R.id.stopTrackingButton);

        startTrackingButton.setClickable(true);
        stopTrackingButton.setClickable(true);

    }

    @Override
    //Used to request the permission from the user to use location services
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableButtons();
            }else{
                runtimePermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Checking if the receiver exists yet
        if(broadcastReceiver==null){
            //Creating the broadcast receiver
            broadcastReceiver = new BroadcastReceiver() {
                //Called when the receiver receives a broadcast
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Checks if this is the start of the activity, if so sets initial time to the current time
                    if(topSpeed == 0 && distanceRan == 0 && initialTime == 0){
                       initialTime = System.currentTimeMillis();
                    }
                    //Gets the speed extra from the broadcasted intent
                    speed = (float)intent.getExtras().get("speed");

                    //Increments distance ran with the new movement sent by the intent
                    distanceRan += (float)intent.getExtras().get("distanceRan");

                    //Rounds up distanceRan as it usually becomes a long number
                    distanceRan = Math.round(distanceRan);

                    //Converts the speed to km/H
                    speed = speedToKilometresHour(speed);

                    //Checks if newly acquired speed is larger than the top speed, if so sets top speed to this speed
                    if(speed > topSpeed){
                        topSpeed = speed;
                    }

                    //Setting the textviews to the new values broadcasted
                    speedView.setText("" + speed + " km/h");
                    distanceRanView.setText(""+distanceRan+" m");
                }
            };
        }
        //Registering the receiver with the appropriate intent filter
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }


    public float speedToKilometresHour(float speed){
        //Converting the speed from m/s into km/h and rounding up to 2 d.p
        float newSpeed = (float) (Math.round((float) (speed*3.6)*100d)/100d);
        return newSpeed;
    }

    public void beginTracking(View v){
        //Starting the service
       startService(new Intent(this, GPS_Service.class));

       //Making the now tracking announcement visible and making the other invisible
        nowTracking.setVisibility(View.VISIBLE);
        finishTracking.setVisibility(View.INVISIBLE);
    }

    public void stopTracking(View v){
        //Stopping the service
        stopService(new Intent(this, GPS_Service.class));

        //Making the announcement of tracking invisible and showing the end tracking one
        nowTracking.setVisibility(View.INVISIBLE);
        finishTracking.setVisibility(View.VISIBLE);

        //Checking if any actual activity has been tracked, no point saving empty activity
        if(distanceRan > 0 && topSpeed > 0){
            endTime = System.currentTimeMillis();
            storeLog();
        }
    }

    public void storeLog(){
        //Getting the time values by subtracting final time from start time
        int time = (int) (endTime - initialTime);
        time = (time /1000)%60;

        //Retrieving current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        String date = sdf.format(calendar.getTime());

        //Getting the average speed dividing total distance ran by time
        float averageSpeed = distanceRan/time;
        averageSpeed = speedToKilometresHour(averageSpeed);


        //Creating new exerciseLog and inserting into database using repository
       ExerciseLog exerciseLog = new ExerciseLog(distanceRan,time, date, averageSpeed, topSpeed);
       myRepository.insert(exerciseLog);

       //Setting all values back to 0
        distanceRan = 0;
        topSpeed = 0;
        initialTime = 0;
        endTime = 0;
        speed = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Making sure the broadcast receiver is unregistered
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void finish(View v){
        finish();
    }
}