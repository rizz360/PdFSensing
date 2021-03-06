package com.rizz.pdf.pdfsensing.Handlers;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rizz on 01/10/2015.
 * Handles currently applicable meta data sets
 */
public class MetaDataHandler {
    private static final String LOG_TAG = "MetaDataHandler";
    private static MetaDataHandler metaDataHandler = null;
    private MetaDataHandler() {
    }
    public static void init() {
        if(metaDataHandler == null) metaDataHandler = new MetaDataHandler();
    }

    private static String formattedDate = "";
    private static List<String> metaData = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    private static void updateDate() {
        Date date = new Date(System.currentTimeMillis());
        formattedDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
    }
    public static void update() {
        updateDate();
        LocationHandler.updateLocation();
        Log.i(LOG_TAG, "Meta Data updated");
    }
    public static String getFormattedDate() {
        return formattedDate;
    }

    public static List<String> getMetaData() {
        metaData.add("Recording Filename: " + FileHandler.getRecFileName());
        metaData.add("Metadata Filename: " + FileHandler.getMetaFileName());
        metaData.add("Recording length: " + AudioHandler.getLastRecordingsLength() + "ms");
        //crashes application without tracestack
        //metaData.add("Max Amplitude: " + AudioHandler.getMaxAmplitude());
        metaData.add("Date: " + MetaDataHandler.getFormattedDate());
        //metaData.add("Location: " + LocationHandler.getCityName());
        metaData.add("Longitude: " + LocationHandler.getLon());
        metaData.add("Latitude: " + LocationHandler.getLat());
        //metaData.add("Altitude: " + LocationHandler.getLastKnownLocation().getAltitude());
        //metaData.add("Accuracy: " + LocationHandler.getLastKnownLocation().getAccuracy());
        metaData.add("Provider: " + LocationHandler.getLastKnownLocation().getProvider());


        return metaData;
    }

    public static void deleteMetaData() {
        metaData = new ArrayList<>();
    }
}
