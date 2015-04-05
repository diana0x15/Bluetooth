package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Diana on 01.04.2015.
 */
public class Text extends View {

    float x, y;
    String text;
    Paint mPaint;

    // coordinates (x, y), radius
    public Text(Context context, int X, int Y, int size, String S) {
        super(context);
        x = ((float) X);
        y = ((float) Y);
        text = S;
        mPaint = new Paint();
        mPaint.setTextSize((float)size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, y, x, mPaint);
    }
}
