package com.rizz.pdf.pdfsensing.graveyard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;

/**
 * Created by rizz on 01/09/2015.
 */
public final class OrientationHelper {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setRequestedOrientationSensorPortrait(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setRequestedOrientationReversePortrait(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setRequestedOrientationSensorLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setRequestedOrientationReverseLandscape(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}