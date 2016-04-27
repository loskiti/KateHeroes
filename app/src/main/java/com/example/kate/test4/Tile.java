package com.example.kate.test4;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.InputStream;
import java.nio.channels.DatagramChannel;


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
    /**
     * /**
     * Картинка
     */
    private Bitmap bmp;
    private int x;
    private int y;
    private DatagramChannel assetManager;

    public Tile(GameView gameView, Bitmap bmp, int x, int y) {

        this.gameView = gameView;

        this.bmp = bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.x = x; // на размер картинки!!!!!!!!
        this.y = y;

    }

    public static int getTileId(int id) {

        return R.drawable.bad1;
    }


    public void onDraw(Canvas canvas) {
       // int h = 70;
        int h=height;
        int w = width;
        int i=x * w - ((y % 2 == 1) ? w / 2 : 0);
        int j=y * (h / 2 )-h/2;
        canvas.drawBitmap(bmp, x * w - ((y % 2 == 1) ? w / 2 : 0), y * (h / 2 )-h/2, null);
    }
    public void changeWayPoint(Bitmap bmp){
   this.bmp=bmp;
    }

}
