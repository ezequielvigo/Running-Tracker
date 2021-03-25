package com.example.runningtracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        //Creating the location listener for the updates
        listener = new LocationListener() {
            //The values that will allow me to calculate distance
            Location location = null;
            float distanceRan = 0;

            //Called whenever a new location is updated
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Checks if location has been set to anything
                if(this.location == null){
                    this.location = location;
                }else{
                    //Calculates distance between current location and new location
                    distanceRan = this.location.distanceTo(location);
                    this.location = location;
                }
                //Creates an intent filter and puts distanceRand and current speed as extras to be broadcasted
                Intent i = new Intent("location_update");
                i.putExtra("distanceRan",distanceRan);
                i.putExtra("speed",location.getSpeed());
                sendBroadcast(i);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                //Redirects user to settings when the provider is disabled
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        //Initialising the location manager with the application context and request updates
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, listener);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Makes sure to remove the update listener
        if(locationManager!=null){
             locationManager.removeUpdates(listener);
        }
    }
}
