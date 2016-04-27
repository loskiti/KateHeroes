package com.example.kate.test4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.Random;

public class Sprite  {
    /**
     * Рядков в спрайте = 4
     */
    private static final int BMP_ROWS = 4;
    /**
     * Колонок в спрайте = 3
     */
    private static final int BMP_COLUMNS = 3;
    /**
     * Объект класса GameView
     */

    private GameView gameView;
    public MapWay mapway;
    /**
     * Картинка
     */
    private Bitmap bmp;

    /**
     * Позиция по Х=0
     */
    private double x ;
    private double y;
    private  int minAttack;
    private  int maxAttack;
    private  int defence;
    private  int damage;
    private int health;
    private  int step;
    private  int initiative;
    private  int morale;
    private int outlive;
    private int number;
    private static int h=72;
    private static int w=120;
    private boolean isBot;
    private Context context;

private int isoX;
    private int isoY;
    /**
     * Скорость по Х=5
     */
    private int xSpeed = 0;
private int newDefence =defence;
    private int ySpeed = 0;
    // direction = 0 up, 1 left, 2 down, 3 right,
// animation = 3 up, 1 left, 0 down, 2 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    /**
     * Текущий кадр = 0
     */
    private int currentFrame = 0;

    /**
     * Ширина
     */
    private int width;

    /**
     * Ввыоста
     */
    private int height;

    /**
     * Конструктор
     */
    public Sprite(Context context,GameView gameView, Bitmap bmp, double x, double y,int damage,
                  int defence,int minAttack, int maxAttack,int health,int step, int initiative, int morale, int number, boolean isBot) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.x = x; // на размер картинки!!!!!!!!
        this.y = y;
        this.minAttack=minAttack;
        this.maxAttack=maxAttack;
        this.defence= defence;
        this.damage=damage;
        this.health=health;
        this.step=step;
        this.initiative=initiative;
        this.morale=morale;
        this.number=number;
        outlive=number*health;
        this.isBot=isBot;
        this.context=context;

    }

    /**
     * Перемещение объекта, его направление
     */
    private void update() {
        if (mapway!=null) {

            if (mapway.way.size()  != 0) {
                Point point;
                point = mapway.way.get(mapway.way.size() - 1);
               /* if(point.y!=y && (x==point.x || x-((y%2==1)?0.25:-0.25)==point.x))
                {
                    x=x+((y%2==1)?0.25:-0.25);
                   // if(point.y>y)
                      //  x=x-0.25;
                   // else x=x+0.25;
                    currentFrame = ++currentFrame % BMP_COLUMNS;
                    return;
                }*/
                if (point.y != y && point.y!=(float)(y+0.04) && point.y!=(float)(y-0.04))
                {

                    if (point.y < y) {
                        ySpeed = -1;
                        xSpeed=0;
                        y = y - 0.24;


                    } else {
                        y = y + 0.24;
                        ySpeed=1;
                        xSpeed=0;


                    }


                    // }
                } else
                if (point.x!=x && point.x!=(float)(x+0.5)&& point.x!=(float)(x-0.5)) {

                    if (point.x < x) {
                        xSpeed = -xSpeed;
                        x=x-0.24;
                        ySpeed=0;
                        xSpeed=-1;
                    }
                  else {
                        x = x + 0.24;
                        ySpeed=0;
                        xSpeed=1;
                    }
                } /*else {*/

if((x==(double)point.x||(x+0.04==(double)point.x)||(x-0.04==(double)point.x)||((float)(x-0.5)==point.x)||((float)(x+0.5)==point.x))
        &&( (y==(double)point.y)||(y+0.04==(double)point.y)||(y-0.04==(double)point.y))) {
    currentFrame = ++currentFrame % BMP_COLUMNS;
    return;
}

            }
        }
        currentFrame = ++currentFrame % BMP_COLUMNS;

    }

    /**
     * Рисуем наши спрайты
     */
    public void onDraw(Canvas canvas) {

        update();

        isoX=(int)(x*w - ((y % 2 == 1) ? w /2 : 0))+w/8;
        isoY =(int)( y * (h / 2)-(h*0.75));
        if(mapway!=null) {
            if (mapway.way.size() != 0) {
                Point point;
                point = mapway.way.get(mapway.way.size() - 1);

                if (((x==(double)point.x||(float)(x+0.04)==(float)point.x)||((float)(x-0.04)==(float)point.x)||((float)(x-0.5)==(float)point.x)||((float)(x+0.5)==(float)point.x))
                        && ((y == (double) point.y) || ((float)(y + 0.04) == (float) point.y) || ((float)(y - 0.04) == (float) point.y))) {
                    y = point.y;
                   // if(((float)(x-0.5)==(double)point.x)||((float)(x+0.5)==(double)point.x))

                    x=point.x;
                    mapway.way.remove(mapway.way.size() - 1);
                }

            }
            else {
                ySpeed=0;
                xSpeed=0;
            }

        }
        int srcX = currentFrame * width;
        int srcY = height*getAnimationRow();
       // int srcY = height;

        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(isoX, isoY, isoX + width, isoY + height);
        canvas.drawBitmap(bmp, src, dst, null);


    }

    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        if (ySpeed==0 && xSpeed==0)
            direction=0+((isBot==true)?1:3);
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }
    /**
     * Проверка на столкновения
     */
    public boolean isCollision(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
//урон, который будет нанесен
public int getAttack (int oppX, int oppY,int oppDefence,int flag){
    int att=0;
    Random random = new Random();
    att=number*(minAttack+(random.nextInt(maxAttack-minAttack)+1))*
            (int) ((minAttack<oppDefence)?(1/(1+(oppDefence-minAttack)*0.05)):(1+(minAttack-oppDefence)*0.05));
    //если стрелок
    if( flag!=0)
    att= (int) (att*((Math.abs(oppX-x)<6 && Math.abs(oppY-y)<6)?1:0.5));
    return att;
}
    //атака
    public int attack(int att){


    outlive=outlive-att;
    number=outlive/health+1;

        return number;
    }
    public int getDefence(){
        return newDefence;
    }
public int getInit(){
    return initiative;
}
public int getNumber(){return number;}
    public int getStep(){return step;}
    public void changeDefence(boolean flag){
        if (flag)
        newDefence=(int)(defence*1.3);
        else
            newDefence=defence;
    }




   /* public static  Sprite createSprite(int resouce,int x,int y) {
       // Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        File f = new File("C:\\Users\\Kate\\AndroidStudioProjects\\Test3\\app\\src\\main\\res\\drawable\\bad1.png");

       // InputStream ims = getActivity().getAssets().open("cat.jpg");
        //ImageView image = (ImageView) getActivity().findViewById(R.id.test_image);
        Bitmap bmp1 =BitmapFactory.decodeFile(f.getPath());
     return new Sprite( bmp1,x,y);
}*/
}