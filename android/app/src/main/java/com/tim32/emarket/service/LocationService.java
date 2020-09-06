package com.tim32.emarket.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import org.androidannotations.annotations.EBean;
import org.jetbrains.annotations.NotNull;

@EBean
public class LocationService {

    private static final String TAG = LocationService.class.getName();

    private Location currentLocation;

    private boolean locationAvailable;

    public void listenToPhoneLocationChangesIfAllowed(Activity activity) {
        LocationListener locationListener = buildLocationListener();
        LocationUtil.trackUserLocationIfAllowed(activity, locationListener);
        locationAvailable = true;
    }

    public void stopListeningToPhoneLocationIfAllowed() {
        locationAvailable = false;
    }

    public boolean isLocationAvailable() {
        return locationAvailable && currentLocation != null;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @NotNull
    private LocationListener buildLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    @SuppressLint("LogNotTimber")
    private void locationChanged(Location location) {
        this.currentLocation = location;
        Log.i(TAG, "Phone location updated");
    }
}
