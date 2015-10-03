package com.rizz.pdf.pdfsensing.Handlers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.ImageView;

import com.rizz.pdf.pdfsensing.R;

/**
 * Created by Rizz on 01/10/2015.
 * Handles the status of the icons that indicate whether the user is using the app correctly
 */
public class StatusHandler {
    private static String LOG_TAG = "StatusHandler";
    private static StatusHandler statusHandler = null;
    private static ImageView gpsView = null;
    private static ImageView orientationView = null;
    private static ImageView angleView = null;
    private static Context baseContext = null;
    private static Resources res = null;
    private StatusHandler() {
    }

    public static void init(Activity activity) {
        if(statusHandler == null) statusHandler = new StatusHandler();

        gpsView = (ImageView) activity.findViewById(R.id.gps_indicator);
        orientationView = (ImageView) activity.findViewById(R.id.orientation_indicator);
        angleView = (ImageView) activity.findViewById(R.id.angle_indicator);
        res = activity.getResources();
        baseContext = activity.getBaseContext();
        activateListeners();
    }

    public static void updateStatus(ImageView item, boolean status) {
        if(status)  setColor(item,R.color.positive);
        else        setColor(item, R.color.negative);
    }

    //stupid hack
    private static void setColor(ImageView item, int color) {
        item.setColorFilter(res.getColor(color));
    }

    public enum GPSSTATE{FULL, PARTIAL, NONE}
    public static void updateGPSStatus(GPSSTATE state) {
        if(gpsView == null) return;
        if(state == GPSSTATE.FULL) setColor(gpsView, R.color.positive);
        if(state == GPSSTATE.PARTIAL) setColor(gpsView, R.color.neutral);
        if(state == GPSSTATE.NONE) setColor(gpsView, R.color.negative);
        Log.i(LOG_TAG, "Status updates to: " + state);
    }

    private static void activateListeners() {
        OrientationEventListener orientationListener = new OrientationEventListener(baseContext) {
            @Override
            public void onOrientationChanged(int i) {
                boolean orState = false;
                if(i == ORIENTATION_UNKNOWN) orState = true; //near horizontal orientation
                if(i < 200 && i > 160) orState = true;
                updateStatus(orientationView, orState);
            }
        };
        orientationListener.enable();
        LocationHandler.enableListener();
    }


}
