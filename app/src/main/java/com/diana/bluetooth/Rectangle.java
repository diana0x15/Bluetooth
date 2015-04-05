package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class Rectangle extends View {

    float x, y, h, w;
    Paint mPaint;

    public Rectangle(Context context, int X, int Y, int H, int W, int c) {
        super(context);
        x = (float) X;
        y = (float) Y;
        h = (float) H;
        w = (float) W;
        mPaint = new Paint();
        if(c == 0) mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(x, y, h, w, mPaint);
    }
}