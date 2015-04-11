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
    public Oval(Context context, int X, int Y, int R, int type) {
        super(context);
        x = ((float) X);
        y = ((float) Y);
        r = ((float) R);
        mPaint = new Paint();
        if(type == 0) mPaint.setStyle(Paint.Style.STROKE);

        int c = Color.rgb(MessagesActivity.r, MessagesActivity.g, MessagesActivity.b);
        mPaint.setColor(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, r, mPaint);
    }
}
