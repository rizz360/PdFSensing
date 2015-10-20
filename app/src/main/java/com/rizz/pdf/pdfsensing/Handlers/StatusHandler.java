package com.rizz.pdf.pdfsensing.Handlers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.ImageView;

import com.rizz.pdf.pdfsensing.ArcRecordTimer;
import com.rizz.pdf.pdfsensing.R;
import com.rizz.pdf.pdfsensing.ShakeEventListener;

/**
 * Created by Rizz on 01/10/2015.
 * Handles the status of the icons that indicate whether the user is using the app correctly
 */
public class StatusHandler {
    private static final int SHAKE_COUNT_THRESHOLD = 2;
    private static String LOG_TAG = "StatusHandler";
    private static StatusHandler statusHandler = null;
    private static ImageView gpsView = null;
    private static ImageView orientationView = null;
    private static ArcRecordTimer recordButton = null;
    private static Activity activity;
    private static Context baseContext = null;
    private static Resources res = null;
    private static ImageView shakeView = null;
    private static SensorManager senMan;
    private static ShakeEventListener shakeListener;
    private static Sensor accelerometer;
    private static CountDownTimer readyStateCheckTimer;

    private StatusHandler() {
    }

    public static void init(Activity act) {
        if(statusHandler == null) statusHandler = new StatusHandler();
        activity = act;
        res = activity.getResources();
        baseContext = activity.getBaseContext();

        gpsView = (ImageView) activity.findViewById(R.id.gps_indicator); gpsView.setEnabled(false);
        orientationView = (ImageView) activity.findViewById(R.id.orientation_indicator); orientationView.setEnabled(false);
        shakeView = (ImageView) activity.findViewById(R.id.shake_indicator); shakeView.setEnabled(true);
        recordButton = (ArcRecordTimer) activity.findViewById(R.id.arcRecTimer);

        activateOrientationListener();
        LocationHandler.enableListener();
        activateShakeListener();

        readyStateCheckTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                recordButton.setEnabled(checkReadyState());
                updateReadyState();
            }
        };
        updateReadyState();
    }

    public static void updateStatus(ImageView item, boolean status) {
        if(status)  setColor(item,R.color.positive);
        else        setColor(item, R.color.negative);
        item.setEnabled(status);
    }

    public static void updateShakeStatus(boolean status) {
        shakeView.setEnabled(status);
        updateStatus(shakeView, status);
    }

    //stupid hack
    private static void setColor(ImageView item, int color) {
        item.setColorFilter(res.getColor(color));
    }

    public static void updateGPSStatus(GPSSTATE state) {
        if(gpsView == null) return;
        gpsView.setEnabled(true);
        if(state == GPSSTATE.FULL) setColor(gpsView, R.color.positive);
        if(state == GPSSTATE.PARTIAL) setColor(gpsView, R.color.neutral);
        if (state == GPSSTATE.NONE) {
            setColor(gpsView, R.color.negative);
            gpsView.setEnabled(false);
        }
    }

    private static void updateReadyState() {
        readyStateCheckTimer.start();
    }

    public static boolean checkReadyState() {
        return (isStatusReady(gpsView) && isStatusReady(orientationView) && isStatusReady(shakeView));
    }

    private static boolean isStatusReady(ImageView view) {
        return view.isEnabled();
    }

    private static void activateOrientationListener() {
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
    }

    private static void activateShakeListener() {
        senMan = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = senMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new ShakeEventListener();
        shakeListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count > SHAKE_COUNT_THRESHOLD) {
                    StatusHandler.updateShakeStatus(false);
                    CountDownTimer resetTimer = new CountDownTimer(1500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            onShake(0);
                        }
                    };
                    resetTimer.start();
                } else
                    StatusHandler.updateShakeStatus(true);
            }
        });
        senMan.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_UI);

    }

    public enum GPSSTATE {FULL, PARTIAL, NONE}
}
