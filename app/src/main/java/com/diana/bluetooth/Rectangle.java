package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class Rectangle extends View {

    float x, y, h, w;
    Paint mPaint;

    public Rectangle(Context context, int X, int Y, int H, int W, int type) {
        super(context);
        x = (float) X;
        y = (float) Y;
        h = (float) H;
        w = (float) W;
        mPaint = new Paint();
        if(type == 0) mPaint.setStyle(Paint.Style.STROKE);

        int c = Color.rgb(MessagesActivity.r, MessagesActivity.g, MessagesActivity.b);
        mPaint.setColor(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(x, y, x+h, y+w, mPaint);
    }
}
