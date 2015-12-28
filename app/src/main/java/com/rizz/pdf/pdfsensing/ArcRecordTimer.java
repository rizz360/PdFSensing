package com.rizz.pdf.pdfsensing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;

import com.rizz.pdf.pdfsensing.Handlers.AudioHandler;
import com.rizz.pdf.pdfsensing.Handlers.StatusHandler;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Rizz on 02/10/2015.
 * Used to display the record timer of 10 seconds
 */
public class ArcRecordTimer extends ArcTimer {
    private static String LOG_TAG = "ArcRecordTimer";
    private final int STROKE_WIDTH = 7;
    private final long TIMER_DURATION_MS = 100000L;
    private final long TIMER_UPDATE_INTERVAL_MS = 50L;
    private Paint arcPaint;
    private Paint textPaint;
    private Paint dotPaint;
    private boolean isEnabled = true;
    private RectF bigOval;  //is basically the frame in which the Arc is allowed to move
    private boolean useCenter;
    private CountDownTimer cdt;
    private float start = 0f;
    private float sweep = 360;
    private int secondsRemaining = (int)TIMER_DURATION_MS/1000;
    private float detailSecondsRemaining = (float)TIMER_DURATION_MS/1000;
    private float radius = 30;
    private boolean growing = false;
    private DecimalFormat df;

    public ArcRecordTimer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        arcPaint = new Paint();
        arcPaint.setColor(context.getResources().getColor(R.color.active));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
        arcPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.active));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(95f);

        dotPaint = new Paint();
        dotPaint.setColor(context.getResources().getColor(R.color.active));
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setAntiAlias(true);

        int offset = STROKE_WIDTH - 3;
        bigOval = new RectF(offset, offset, context.getResources().getDimension(R.dimen.icon_width) - offset, context.getResources().getDimension(R.dimen.icon_height) - offset);
        useCenter = false;

        df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void startCountdown() {
        secondsRemaining = 100;
        if(cdt != null) {
            stopCountDown();
            sweep = 360;
            return;
        }
        AudioHandler.startRecording();
        Log.i(LOG_TAG, "Timer started");
        cdt = new CountDownTimer(TIMER_DURATION_MS, TIMER_UPDATE_INTERVAL_MS) {
            @Override
            public void onTick(long l) {    //l = ms until finish
                secondsRemaining = (int) Math.ceil((float)l/1000);
                detailSecondsRemaining = (float)l/1000;
                float percentage = ((float)l/TIMER_DURATION_MS);
                sweep = 360 * percentage;
            }

            @Override
            public void onFinish() {
                stopCountDown();
                AudioHandler.stopRecording();
                StatusHandler.vibrate();
            }
        }.start();
    }

    public void stopCountDown() {
        if (!AudioHandler.isRecording()) return;
        cdt.cancel();
        Log.i(LOG_TAG, "Timer stopped");
        sweep = 360;
        secondsRemaining = (int)TIMER_DURATION_MS/1000;
        detailSecondsRemaining = (float)TIMER_DURATION_MS/1000;
        cdt = null;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        //Draw the actual countdown arc
        canvas.drawArc(bigOval, start, 360, useCenter, arcPaint);

        if (!isEnabled) {
            canvas.drawLine(0, 0, bigOval.width(), bigOval.height(), arcPaint);
            invalidate();
            return;
        }

        //Draw middle portion displaying the remaining time in seconds
        //or a simple dot to indicate that recording can start

        //if(secondsRemaining >= TIMER_DURATION_MS/1000) {
        if(detailSecondsRemaining >= TIMER_DURATION_MS/1000) {
            //draw additional pulse
            if(radius <= 30) growing = true;
            if(radius >= 45) growing = false;
            if (growing) radius *= 1.02;
            if(!growing) radius /= 1.02;

            canvas.drawCircle(bigOval.centerX(), bigOval.centerY(), radius, dotPaint);

            //draw record dot
            canvas.drawCircle(bigOval.centerX(), bigOval.centerY(), 30, dotPaint);
        }
        else {
            drawCenteredText(canvas, "\u25A0", bigOval, textPaint);
        }
        invalidate();
    }
}
