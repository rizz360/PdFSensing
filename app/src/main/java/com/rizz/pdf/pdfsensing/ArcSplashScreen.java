package com.rizz.pdf.pdfsensing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by rizz on 17/10/2015.
 */

public class ArcSplashScreen extends ArcTimer {
    private static String LOG_TAG = "ArcSplashScreen";
    private final int STROKE_WIDTH = 30;
    //private final int SIZE = R.dimen.splash_loading_size; //gigant number?
    boolean useCenter;

    private Paint arcPaint;
    private RectF bigOval;  //is basically the frame in which the Arc is allowed to move

    private float start = 0f;
    private float sweep = 360f;
    private float push = 0f;


    public ArcSplashScreen(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        arcPaint = new Paint();
        arcPaint.setColor(context.getResources().getColor(R.color.positive));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
        arcPaint.setAntiAlias(true);

        int size = 400 - STROKE_WIDTH;
        bigOval = new RectF(STROKE_WIDTH, STROKE_WIDTH, size, size);
        useCenter = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(sweep >= 180) push -= 0.3;
        else push += 0.3;
        sweep += push;

        start++;

        while(sweep >= 360) sweep -= 360;
        while(start >= 360) start -= 360;

        canvas.drawArc(bigOval, start, sweep, useCenter, arcPaint);
        invalidate();
    }


}
