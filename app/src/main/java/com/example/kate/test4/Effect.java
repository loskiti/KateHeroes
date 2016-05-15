package com.example.kate.test4;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kate on 10.05.2016.
 */
public class Effect {
    private GameView gameView;
    private Bitmap bmp;
    //размеры тайла поля
    private static int h = 72;
    private static int w = 120;
    private int x;
    private int y;
    //выводимые на экран
    private int isoX;
    private int isoY;
    private boolean isStop = false;
    private boolean USE_EFF = true;

    public Effect(GameView gameView, Bitmap bmp, int isoX, int isoY) {

        this.gameView = gameView;
        this.bmp = bmp;
        this.isoX = isoX;
        this.isoY = isoY;
        this.x = isoX;
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

    public void onDraw(Canvas canvas) {
        update();
        int X = (int) (x * w - ((y % 2 == 1) ? w / 2 : 0)) + w / 8;
        int Y = (int) (y * (h / 2) - (h * 0.75));
        if (USE_EFF)
            canvas.drawBitmap(bmp, X, Y, null);
    }

    public boolean getUSE_EFF() {
        return isStop;
    }
}
