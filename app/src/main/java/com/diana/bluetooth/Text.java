package com.diana.bluetooth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by Diana on 01.04.2015.
 */
public class Text extends View {

    float x, y;
    String text;
    Paint mPaint;

    // coordinates (x, y), radius
    public Text(Context context, int X, int Y, String S, int type) {
        super(context);
        x = ((float) X);
        y = ((float) Y);
        text = S;
        mPaint = new Paint();
        mPaint.setTextSize(MessagesActivity.SIZE);

        if(type == 1){
            mPaint.setTypeface(Typeface.MONOSPACE);
        }

        int c = Color.rgb(MessagesActivity.R, MessagesActivity.G, MessagesActivity.B);
        mPaint.setColor(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, x, y, mPaint);
    }
}
