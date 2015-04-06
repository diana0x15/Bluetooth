package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class TextAligned extends View {

    float x, y;
    String text;
    Paint mPaint;

    // coordinates (x, y), radius
    public TextAligned (Context context, int X, int Y, String S) {
        super(context);
        x = ((float) X);
        y = ((float) Y);
        text = S;
        mPaint = new Paint();
        mPaint.setTextSize(MessagesActivity.SIZE);

        Typeface typeface = Typeface.create("Consolas", Typeface.NORMAL);
        mPaint.setTypeface(typeface);

        int c = Color.rgb(MessagesActivity.R, MessagesActivity.G, MessagesActivity.B);
        mPaint.setColor(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, x, y, mPaint);
    }
}
