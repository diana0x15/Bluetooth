package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Line extends View {

    float x1, y1, x2, y2;
    Paint mPaint;

    public Line(Context context, int X1, int Y1, int X2, int Y2) {
        super(context);
        mPaint = new Paint();

        int c = Color.rgb(MessagesActivity.R, MessagesActivity.G, MessagesActivity.B);
        mPaint.setColor(c);

        x1 = (float) X1;
        y1 = (float) Y1;
        x2 = (float) X2;
        y2 = (float) Y2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(x1, y1, x2, y2, mPaint);
    }
}
