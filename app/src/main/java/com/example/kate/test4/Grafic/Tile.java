package com.example.kate.test4.Grafic;


import android.graphics.Bitmap;

import android.graphics.Canvas;


public class Tile implements OnDraw {
    private int width;
    private int height;
    private  Bitmap bmp;
    private  int x;
    private  int y;

    public Tile( Bitmap bmp, int x, int y) {

        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.x = x;
        this.y = y;

    }
    @Override
    public void onDraw(Canvas canvas) {
        int h = height;
        int w = width;
        canvas.drawBitmap(bmp, x * w - ((y % 2 == 1) ? w / 2 : 0), y * (h / 2) - h / 2, null);
    }

    public void setWayPoint(Bitmap bmp) {
        this.bmp = bmp;
    }

}
