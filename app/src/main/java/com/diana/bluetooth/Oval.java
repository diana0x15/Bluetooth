package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Diana on 25.03.2015.
 */
public class Oval extends View {

    float x, y;
    float r;
    Paint mPaint;

    // coordinates (x, y), radius
    public Oval(Context context, int X, int Y, int R, int c) {
        super(context);
        x = ((float) Y);
        y = ((float) Y);
        r = ((float) R);
        mPaint = new Paint();
        if(c == 0) mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, r, mPaint);
    }
}
