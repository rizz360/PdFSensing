package com.rizz.pdf.pdfsensing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;

import com.rizz.pdf.pdfsensing.Handlers.AudioHandler;

/**
 * Created by Rizz on 02/10/2015.
 * Used to display the play timer - depends on length of last recording
 */
public class ArcPlayTimer extends ArcTimer {
    private static String LOG_TAG = "ArcPlayTimer";
    private Paint arcPaint;
    private Paint textPaint;
    private Paint dotPaint;

    private RectF bigOval;  //is basically the frame in which the Arc is allowed to move
    private boolean useCenter;
    private final int STROKE_WIDTH = 7;

    private CountDownTimer cdt;
    private final long TIMER_UPDATE_INTERVAL_MS = 50L;
    private float start = 0f;
    private float sweep = 360;
    private int recordingLengthMS = 0;
    private String playState = "";

    public ArcPlayTimer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        arcPaint = new Paint();
        arcPaint.setColor(context.getResources().getColor(R.color.positive));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
        arcPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.positive));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(120f);

        dotPaint = new Paint();
        dotPaint.setColor(context.getResources().getColor(R.color.positive));
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setAntiAlias(true);

        int offset = STROKE_WIDTH - 3;
        bigOval = new RectF(offset, offset, context.getResources().getDimension(R.dimen.icon_width) - offset, context.getResources().getDimension(R.dimen.icon_height) - offset);
        useCenter = false;
    }

    public void startCountdown() {
        if(cdt != null) {
            stopCountDown();
            return;
        }

        AudioHandler.startPlaying();
        playState = "\u25A0";
        recordingLengthMS = AudioHandler.getLastRecordingsLength();
        Log.e(LOG_TAG, "record length = " + recordingLengthMS);

        Log.i(LOG_TAG, "Timer started");
        cdt = new CountDownTimer(recordingLengthMS, TIMER_UPDATE_INTERVAL_MS) {
            @Override
            public void onTick(long l) {    //l = ms until finish
                float percentage = ((float)l/recordingLengthMS);
                sweep = 360 * percentage;
            }

            @Override
            public void onFinish() {
                stopCountDown();
            }
        }.start();
    }

    private void stopCountDown() {
        cdt.cancel();
        AudioHandler.stopPlaying();
        Log.i(LOG_TAG, "Timer stopped");
        sweep = 360;
        playState ="\u25B6";
        cdt = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw the actual countdown arc
        canvas.drawArc(bigOval, start, sweep, useCenter, arcPaint);
        drawCenteredText(canvas, playState, bigOval, textPaint);
        invalidate();
    }
}
