package com.rizz.pdf.pdfsensing;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Rizz on 04/10/2015.
 * Listens for device shakenings
 * mainly: http://jasonmcreynolds.com/?p=388
 */
public class ShakeEventListener implements SensorEventListener {

    private final float SHAKE_THRESHOLD_GRAVITY = 2.0F;
    private final int SHAKE_SLOP_TIME_MS = 500;
    private final int SHAKE_COUNT_RESET_TIME_MS = 1000;

    private long mShakeTimestamp;
    private int mShakeCount;

    private OnShakeListener shakeListener;

    public void setOnShakeListener(OnShakeListener listener) {
        this.shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (shakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                shakeListener.onShake(mShakeCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        void onShake(int count);
    }
}
