package com.rizz.pdf.pdfsensing.Handlers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rizz on 01/10/2015.
 * Handles user's current location (lon, lat and city name)
 * and gives general interface to access these information
 */
public class LocationHandler {
    private static final String LOG_TAG = "LocationHandler";
    private static LocationHandler locationHandler = null;
    private LocationHandler() {
    }

    public static void init(Context bc) {
        if(locationHandler == null) locationHandler = new LocationHandler();
        baseContext = bc;
        if(baseContext != null)
            locationManager = (LocationManager)baseContext.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null)
            updateLocation();
    }

    //http://www.rdcworld-android.blogspot.in/2012/01/get-current-location-coordinates-city.html
    private static LocationManager locationManager = null;
    private static Context baseContext = null;

    private static Location lastKnownLocationGPS;
    private static Location lastKnownLocationNETWORK;
    private static String cityName = "not yet implemented";
    private static double lon = 0.0;
    private static double lat = 0.0;


    public static void updateLocation() {
        try {
            if(locationManager != null) {
                StatusHandler.updateGPSStatus(StatusHandler.GPSSTATE.NONE);
                lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lastKnownLocationNETWORK = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(lastKnownLocationNETWORK != null) StatusHandler.updateGPSStatus(StatusHandler.GPSSTATE.PARTIAL);
                if(lastKnownLocationGPS != null) StatusHandler.updateGPSStatus(StatusHandler.GPSSTATE.FULL);
            }
        }
        catch(SecurityException se) {
            Log.e(LOG_TAG, "User did not give permission to location services");
        }
    }

    public static Location getLastKnownLocation() {
        if(lastKnownLocationGPS == null) return lastKnownLocationNETWORK;
        return lastKnownLocationGPS;
    }

    public static void enableListener() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(LOG_TAG, "Location changed: Lon=" + location.getLongitude() + " / Lat=" + location.getLatitude() + " from provider: " + location.getProvider() + " with accuracy " + location.getAccuracy());
                updateLocation();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                updateLocation();
            }

            @Override
            public void onProviderEnabled(String s) {
                updateLocation();
            }

            @Override
            public void onProviderDisabled(String s) {
                updateLocation();
            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch(SecurityException se) {
            Log.e(LOG_TAG, "User did not give permission to location services");
        }
    }

    public static double getLon() {
        if(lastKnownLocationNETWORK != null)  lon = lastKnownLocationNETWORK.getLongitude();
        if(lastKnownLocationGPS != null)  lon = lastKnownLocationGPS.getLongitude();
        return lon;
    }

    public static double getLat() {
        if(lastKnownLocationNETWORK != null)  lat = lastKnownLocationNETWORK.getLatitude();
        if(lastKnownLocationGPS != null)  lat = lastKnownLocationGPS.getLatitude();
        return lat;
    }

    //Reverse lookup of current town/city via lon/lat - needs further implementation
    public static String getCityName() {
        if(lastKnownLocationGPS != null || lastKnownLocationNETWORK != null) {
            Geocoder gcd = new Geocoder(baseContext, Locale.getDefault());
            List<Address> addressList;
            try {
                addressList = gcd.getFromLocation(getLon(), getLat(), 1);
                if(!addressList.isEmpty()) cityName = addressList.get(0).getLocality();
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error updating location");
                e.printStackTrace();
            }
        }
        else
            Log.e(LOG_TAG, "Location not found yet");
        return cityName;
    }
}