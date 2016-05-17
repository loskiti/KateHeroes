package com.example.kate.test4.Grafic;


import android.graphics.Bitmap;

import android.graphics.Canvas;

import com.example.kate.test4.Base.GameView;
import com.example.kate.test4.R;


public class Tile {
    private int currentFrame = 0;
    private static int width;
    private int height;
    private GameView gameView;
    private static final int BMP_ROWS = 4;
    /**
     * Колонок в спрайте = 3
     */
    private static final int BMP_COLUMNS = 3;
    private Bitmap bmp;
    private int x;
    private int y;

    public Tile(GameView gameView, Bitmap bmp, int x, int y) {

        this.gameView = gameView;

        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.x = x;
        this.y = y;

    }

    public static int getTileId(int id) {

        return R.drawable.bad1;
    }


    public void onDraw(Canvas canvas) {
        int h = height;
        int w = width;
        int i = x * w - ((y % 2 == 1) ? w / 2 : 0);
        int j = y * (h / 2) - h / 2;
        canvas.drawBitmap(bmp, x * w - ((y % 2 == 1) ? w / 2 : 0), y * (h / 2) - h / 2, null);
    }

    public void changeWayPoint(Bitmap bmp) {
        this.bmp = bmp;
    }

}