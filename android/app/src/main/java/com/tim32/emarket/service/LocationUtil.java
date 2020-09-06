package com.tim32.emarket.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.tim32.emarket.util.PermissionUtil;

public final class LocationUtil {

    @SuppressLint("MissingPermission")
    public static Location trackUserLocationIfAllowed(Activity activity, LocationListener locationListener) {
        LocationManager locationManager;
        String locationServiceContext = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) activity.getSystemService(locationServiceContext);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }

        if (PermissionUtil.isLocationPermissionGranted(activity)) {
            locationManager.requestLocationUpdates(provider, 0, 20, locationListener);
            return locationManager.getLastKnownLocation(provider);
        }

        return null;
    }

    private LocationUtil() {
    }
}
