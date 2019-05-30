package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;

public class FieldView extends View
{
    Paint paint;
    private int[][] positions = new int [10][10];

    boolean flag = true;

    public FieldView(Context context, int positions[][])
    {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                this.positions[i][j] = positions[i][j];
            }
        }
    }

     public FieldView(Context context, int positions[][], boolean flag)
    {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                this.positions[i][j] = positions[i][j];
            }
        }

        this.flag = flag;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(0, 0,0,0);

        paint.setColor(Color.BLUE);
        drawGrid(canvas);
    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < 660; i += 60) {
            canvas.drawLine(80 + i, 80, 80 + i, 680, paint);
        }
        for (int i = 0; i < 660; i += 60) {
            canvas.drawLine(80, 80 + i, 680, 80 + i, paint);
        }

        for (int i = 1; i < 11; i += 1) {
            canvas.drawText(Integer.toString(i), 50 + i*60, 65,   paint);
        }

        for (int i = 1; i < 11; i += 1) {
            canvas.drawText(Character.toString ((char)('A' + (char) (i - 1))),  55, 70 + i*60,   paint);
        }

        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                paint.setColor(Color.BLUE);

                switch (positions[i][j])
                {
                    case 5:
                        canvas.drawLine(80 + j*60, 80 + i*60, 80 + (j+1)*60, 80 + (i+1)*60, paint);
                        canvas.drawLine(80 + j*60, 80 + (i+1)*60, 80 + (j+1)*60, 80 + i*60, paint);

                        break;

                    case 6: canvas.drawCircle(110 + j*60, 110 + i*60, 5, paint); break;

                    case 7:

                        paint.setColor(Color.RED);

                        canvas.drawLine(80 + j*60, 80 + (i+1)*60, 80 + (j+1)*60, 80 + i*60, paint);
                        canvas.drawLine(80 + j*60, 80 + i*60, 80 + (j+1)*60, 80 + (i+1)*60, paint);
                        canvas.drawLine(110 + j*60, 80 + i*60, 80 + j*60, 110 + i*60, paint);
                        canvas.drawLine(110 + j*60, 80 + (i+1)*60, 80 + (j+1)*60, 110 + i*60, paint);
                        canvas.drawLine(110 + j*60, 80 + i*60, 80 + (j+1)*60, 110 + i*60, paint);
                        canvas.drawLine(110 + j*60, 80 + (i+1)*60, 80 + j*60, 110 + i*60, paint);

                        break;

                    case 0:
                        break;

                    default:

                        if (flag)
                        {
                            paint.setColor(Color.BLACK);

                            canvas.drawCircle(110 + j*60, 110 + i*60, 10, paint);
                        }
                }
            }
        }
    }

}
