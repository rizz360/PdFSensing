package com.rizz.pdf.pdfsensing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by Rizz on 02/10/2015.
 * General draft as abstract class
 */
public abstract class ArcTimer extends Button {

    public ArcTimer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void startCountdown() {

    }

    public void drawCenteredText(Canvas canvas, String text, RectF locationObj, Paint paint) {
        int textX = (int)(locationObj.centerX() - paint.measureText(text)/2);
        int textY = (int) (locationObj.centerY() - ((paint.descent() + paint.ascent()) / 2)) ;
        canvas.drawText(text, textX, textY, paint);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
    }
}
