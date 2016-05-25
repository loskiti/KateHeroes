package com.example.kate.test4.Grafic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.kate.test4.Map.MapWay;

public class Sprite extends UnitSprite implements OnDraw {
    /**
     * Рядов в спрайте = 4
     */
    private static final int BMP_ROWS = 4;
    /**
     * Колонок в спрайте = 3
     */
    private static final int BMP_COLUMNS = 3;
    public MapWay mapway;
    private int xSpeed = 0;
    private int ySpeed = 0;
       // направление = 0 вверх, 1 влево, 2 вниз, 3 вправо,
// анимация = 3 вверх, 1 влево, 0 вниз, 2 вправо
    int[] DIRECTION_TO_ANIMATION_MAP = {3, 1, 0, 2};
    /**
     * Текущий кадр
     */
    private int currentFrame = 0;
    private int width;
    private int height;

    public Sprite( Bitmap bmp, double x, double y,
                  int defence, int minAttack, int maxAttack, int health, int step, int initiative,  int number, boolean isBot, boolean shot, boolean dead) {
      super(bmp,x,y,defence,minAttack,maxAttack,health,step,initiative,number,isBot,shot,dead);
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;

    }


    private void update() {
        if (mapway != null) {

            if (mapway.way.size() != 0) {
                Point point;
                point = mapway.way.get(mapway.way.size() - 1);
                if (point.y != y && point.y != (float) (y + 0.04) && point.y != (float) (y - 0.04)) {

                    if (point.y < y) {
                        ySpeed = -1;
                        xSpeed = 0;
                        y = y - 0.24;


                    } else {
                        y = y + 0.24;
                        ySpeed = 1;
                        xSpeed = 0;


                    }


                } else if (point.x != x && point.x != (float) (x + 0.5) && point.x != (float) (x - 0.5)) {

                    if (point.x < x) {
                        xSpeed = -xSpeed;
                        x = x - 0.24;
                        ySpeed = 0;
                        xSpeed = -1;
                    } else {
                        x = x + 0.24;
                        ySpeed = 0;
                        xSpeed = 1;
                    }
                }

                if ((x == (double) point.x || (x + 0.04 == (double) point.x) || (x - 0.04 == (double) point.x) || ((float) (x - 0.5) == point.x) || ((float) (x + 0.5) == point.x))
                        && ((y == (double) point.y) || (y + 0.04 == (double) point.y) || (y - 0.04 == (double) point.y))) {
                    currentFrame = ++currentFrame % BMP_COLUMNS;
                    return;
                }

            }
        }
        currentFrame = ++currentFrame % BMP_COLUMNS;

    }

    @Override
    public void onDraw(Canvas canvas) {

        update();
        int w = 120;
        int isoX = (int) (x * w - ((y % 2 == 1) ? w / 2 : 0)) + w / 8;
        int h = 72;
        int isoY = (int) (y * (h / 2) - (h * 0.75));
        if (mapway != null) {
            if (mapway.way.size() != 0) {
                Point point;
                point = mapway.way.get(mapway.way.size() - 1);

                if (((x == (double) point.x || (float) (x + 0.04) == (float) point.x) || ((float) (x - 0.04) == (float) point.x) || ((float) (x - 0.5) == (float) point.x) || ((float) (x + 0.5) == (float) point.x))
                        && ((y == (double) point.y) || ((float) (y + 0.04) == (float) point.y) || ((float) (y - 0.04) == (float) point.y))) {
                    y = point.y;
                    x = point.x;
                    mapway.way.remove(mapway.way.size() - 1);
                }

            } else {
                ySpeed = 0;
                xSpeed = 0;
            }

        }

        int srcX = currentFrame * width;
        int srcY = height * getAnimationRow();

        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(isoX, isoY, isoX + width, isoY + height);
        if (!dead)
            canvas.drawBitmap(bmp, src, dst, null);


    }

    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        if (ySpeed == 0 && xSpeed == 0)
            direction = ((isBot) ? 1 : 3);
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    //атака
    public int attack(int att) {

        outlive = outlive - att;
        number = outlive / health + 1;

        return number;
    }


}