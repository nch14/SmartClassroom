package com.chenh.smartclassroom.view.course;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by carlos on 2016/10/10.
 */

public class EmptyTimeTextView extends CornerTextView {

    public EmptyTimeTextView(Context context, int bgColor, int cornerSize) {
        super(context, bgColor, cornerSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mBgColor);
        paint.setAlpha(50);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mCornerSize, mCornerSize, paint);

        super.onDraw(canvas);
    }
}
