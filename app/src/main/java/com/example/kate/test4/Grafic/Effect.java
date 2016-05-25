package com.example.kate.test4.Grafic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Effect implements OnDraw {
    private static Bitmap bmp;
    private static int x;
    private static int isoY;
    private int y;
    //конечная ячейка

    private boolean isStop = false;
    private boolean USE_EFF = true;

    public Effect( Bitmap bmp, int isoX, int isoY) {

        Effect.bmp = bmp;
        Effect.isoY = isoY;
        x = isoX;
        this.y = 0;

    }

    private void update() {

        if (y < isoY) {
            y = y + 2;
        } else {
            USE_EFF = false;
            isStop = true;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        update();
        //размер 1 тайла
        int w = 120;
        int h = 72;
        int X =  (x * w - ((y % 2 == 1) ? w / 2 : 0)) + w / 8;
        int Y = (int) (y * (h / 2) - (h * 0.75));
        if (USE_EFF)
            canvas.drawBitmap(bmp, X, Y, null);

    }
    public boolean getUSE_EFF() {
        return isStop;
    }
}
