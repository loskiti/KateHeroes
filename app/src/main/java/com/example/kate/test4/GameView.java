package com.example.kate.test4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.sort;

public class GameView extends SurfaceView {
    private List<Sprite> spritesPlayer = new ArrayList<Sprite>();
    private List<Sprite> spritesBot = new ArrayList<Sprite>();
    private List<ATB> roundPlayer = new ArrayList<ATB>();
    private List<ATB> showRoundPlayer = new ArrayList<ATB>();
    private List<Tile> tiles = new ArrayList<Tile>();
private int whoGo=0;
    private int whoDefence;
    private boolean FINISH=false;
    private Map map;
    private long lastClick;

    /**
     * Загружаем спрайт
     */
    private Bitmap bmp;

    final public static boolean USE_ISO = true;
    final public static int OFFSET_MAP_X = 320;
    public boolean FIRST_ROUND=true;
    /**
     * Поле рисования
     */
    private SurfaceHolder holder;
    //0-игрок, 1-бот
    private int whoPlay;
    private boolean USE_STEP=false;
    /**
     * объект класса GameView
     */
    private GameManager gameLoopThread;

    /**
     * Объект класса Sprite
     */
    private Sprite sprite;
    private Tile tile;
private Context context;
    /**
     * Конструктор
     */
    public GameView(Context context) {
        super(context);

        gameLoopThread = new GameManager(this);
        holder = getHolder();
        map = new Map(context);
          /*Рисуем все наши объекты и все все все*/
        holder.addCallback(new SurfaceHolder.Callback() {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder) {

                Tile tile;
                Sprite sprite;
                for (int y = 0; y < map.getMapHeight(); y++) {
                    for (int x = 0; x < map.getMapWidth(); x++) {
                        int id = map.getMapTile(x, y);
                        tiles.add(createTile(map.getTileName(id), x, y));
                    }
                }
                createSprites();
                if (FIRST_ROUND)
                    doOrder();


                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

    }

    private void createSprites() {


        spritesBot.add(createSprite(R.drawable.bad1, 9, 2,5,3,1,2,6,3,12,20,30,true));
        spritesBot.add(createSprite(R.drawable.bad2, 9, 4,3,5,1,2,20,3,10,14,20,true));
        spritesBot.add(createSprite(R.drawable.bad3, 9, 6,6,6,5,7,24,2,7,9,23,true));
        spritesBot.add(createSprite(R.drawable.bad4, 9, 8,12,9,7,7,29,2,10,5,5,true));
        spritesBot.add(createSprite(R.drawable.bad5, 9, 10,14,14,14,19,45,3,12,3,3,true));
        spritesBot.add(createSprite(R.drawable.bad1, 9, 12,27,20,25,35,135,3,8,2,2,true));


        spritesPlayer.add(createSprite(R.drawable.bad1, 1, 2,3,3,1,4,6,2,13,16,27, false));
        spritesPlayer.add(createSprite(R.drawable.bad2, 1, 4, 3, 4, 1, 4, 13, 2, 8, 15, 45, false));
        spritesPlayer.add(createSprite(R.drawable.bad3, 1, 6,4,2,4,6,15,3,13,8,39, false));
        spritesPlayer.add(createSprite(R.drawable.bad4, 1, 8,6,6,6,13,32,1,9,5,6, false));
        spritesPlayer.add(createSprite(R.drawable.bad5, 1, 10,18,17,10,17,66,2,15,3,2, false));
        spritesPlayer.add(createSprite(R.drawable.bad1, 1, 12, 27, 23, 13, 31, 140, 2, 9, 2, 1, false));


    }

   private Sprite createSprite(int resource, int x, int y,int damage,
                               int defence,int minAttack, int maxAttack,int health,int step, int initiative, int morale, int number, boolean isBot) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(getContext(),this, bmp, x, y,damage ,defence,minAttack, maxAttack,health,step, initiative,morale, number, isBot);
    }

    private Tile createTile(int resouce, int x, int y) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Tile(this, bmp, x, y);
    }
    private ATB createATB(int id, int curATB,int startATB, int ini, boolean ifFirstRound,  int sizeInList) {
        int resouce;

        switch (id){
            case 0 : {  resouce= R.drawable.b1; break;}
            case 1 : {  resouce= R.drawable.b2; break;}
            case 2 : {  resouce= R.drawable.b3; break;}
            case 3 : {  resouce= R.drawable.b4; break;}
            case 4 : {  resouce= R.drawable.b5; break;}
            default:{  resouce= R.drawable.b1; break;}
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new ATB(this, bmp, curATB,startATB, ini, ifFirstRound, sizeInList);
    }
    /**
     * Функция рисующая все спрайты и фон
     */
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);
        if(USE_STEP) {
            for(Tile tile:tiles)
            {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                tile.changeWayPoint(bmp);
            }
            USE_STEP=false;
        }

        for (Tile tile : tiles) {
            tile.onDraw(canvas);

        }
        //if(!USE_STEP)
       // clearMap();
        for (Sprite sprite : spritesBot) {
            sprite.onDraw(canvas);

        }
        for (Sprite sprite : spritesPlayer) {
            sprite.onDraw(canvas);
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
         /*   Toast toast = Toast.makeText(getContext(),
                   "tght", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/

        }
        for(int i=0; i<7;i++)
            showRoundPlayer.get(i).onDraw(canvas,i);
        //меню
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.menu2);
        canvas.drawBitmap(bmp1, canvas.getWidth()-bmp1.getWidth() , canvas.getHeight()-bmp1.getHeight() , null);
if(FINISH)
{
    Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.menu3);
    canvas.drawBitmap(bmp2, 0 , 0 , null);

}

       // TextView textView = (TextView)findViewById(R.id.textView2);
// задаём текст
//        textView.setText("Hello Kitty!");
    }

    /**
     * Обработка косания по экрану
     */
    public boolean onTouchEvent(MotionEvent e) {

      //  USE_STEP=false;
       // clearMap();
        if (System.currentTimeMillis() - lastClick > 300) {

            // определение координат нажатия
            lastClick = System.currentTimeMillis();
            int _offX = (int) e.getX();
            int _y = (int) e.getY();
            if(isButton(_offX,_y))
                return false;

            int isoY = (_y / 36) + 1;
            int isoX = (_offX + ((isoY % 2 == 1) ? 60 : 0)) / 120;
            if(isGo(isoX, isoY))
                USE_STEP=true;
            else return false;

            //удар или нет
            // если удар - ближний бой или дальний
            int isAttack = 0;
            int shot = 0;

            if (isWhoPlay() == 0) {
                for (int i = 0; i < spritesBot.size(); i++) {
                    if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY()) {
                        isAttack = 1;
                        whoDefence = i;
                    }


                }
                if (isAttack == 1)
                    if (isWhoGo() == 3)
                        shot = 1;
            } else {
                for (int i = 0; i < spritesPlayer.size(); i++) {
                    if (isoX == (int) spritesPlayer.get(i).getX() && isoY == (int) spritesPlayer.get(i).getY()) {
                        isAttack = 1;
                        whoDefence = i;
                    }
                }
                if (isAttack == 1)
                    if (isWhoGo() == 0 || isWhoGo() == 3)
                        shot = 1;
            }
            Timer mTimer = new Timer();
            MyTimerTask myTimerTask = new MyTimerTask();
            // просто ход или, если ближний бой, то подойти к сопернику
            if (isAttack == 0 || isAttack == 1 && shot != 1)
                if (tileIsPossible(isoX, isoY)) {
                    whoGo = isWhoGo();
                    double x,y;
                    if(isWhoPlay()==0) {
                         x = spritesPlayer.get(whoGo).getX();
                         y = spritesPlayer.get(whoGo).getY();
                    }
                    else {
                         x = spritesBot.get(whoGo).getX();
                         y = spritesBot.get(whoGo).getY();
                    }
                    //если удар, то отходим на шаг назад
                    if (isAttack == 1)
                        isoX = isoX + ((isoX - (int) x > 0) ? -1 : 1);

                    MapWay mapPath = makePath((int) x, (int) y, isoX, isoY);
               /* if(mapPath!=null) {
                    for (int i = 0; i < mapPath.way.size() - 3; i++) {
                        Point point;
                        point = mapPath.way.get(i);
                        Point nextpoint;
                        nextpoint = mapPath.way.get(i + 2);
                        if (point.x == nextpoint.x) mapPath.way.remove(i + 1);
                    }
                }*/
                    if(isWhoPlay()==0)
                    spritesPlayer.get(whoGo).mapway = mapPath;
                        else
                        spritesBot.get(whoGo).mapway = mapPath;
                    if(isAttack==1)
                         doAttack(shot);

                    //clearMap();
                    mTimer.schedule(myTimerTask, mapPath.way.size() * 100 * 5);

                 //   doOrder();

                }

            // если выстрел
            if (isAttack == 1 && shot == 1) {

                doAttack(shot);

                mTimer.schedule(myTimerTask, 100*4);
              // clearMap();
               // doOrder();


            }




        }
       // clearMap();
        return true;
    }
private void doAttack(int shot){
    int damage;
    int outlive;

    if (isWhoPlay() == 0) {

        damage = spritesPlayer.get(isWhoGo()).getAttack((int) spritesBot.get(whoDefence).getX(), (int) spritesBot.get(whoDefence).getY(),
                spritesBot.get(whoDefence).getDefence(), shot);
        outlive = spritesBot.get(whoDefence).attack(damage);
        if(outlive<1) {
            delitInOrder(0);
            spritesBot.remove(whoDefence);

        }

    }
    else {
        damage = spritesBot.get(isWhoGo()).getAttack((int) spritesPlayer.get(whoDefence).getX(), (int) spritesPlayer.get(whoDefence).getY(),
                spritesPlayer.get(whoDefence).getDefence(), shot);
        outlive = spritesPlayer.get(whoDefence).attack(damage);
        if(outlive<1) {
            delitInOrder(6);
            spritesPlayer.remove(whoDefence);

        }
    }



}
    private int isWhoGo(){

       int flag= showRoundPlayer.get(0).getSizeInList();
        flag=flag-((flag-6<0)?0:6);
        return flag;
    }
    private int isWhoPlay(){

        int flag= showRoundPlayer.get(0).getSizeInList();
        if(showRoundPlayer.get(0).getDefence()){
            showRoundPlayer.get(0).changeDefense(false);
        if(isWhoPlay()==0)
            spritesPlayer.get(isWhoGo()).changeDefence(true);
        else spritesBot.get(isWhoGo()).changeDefence(true);
        }
       if(flag-6<0)
        return 1;
        else
           return 0;
    }
    public MapWay makePath(int startX, int startY, int endX, int endY) {
        MapWayFind mfp = new MapWayFind();
        mfp.setcheckPoint(new IMapCheckPoint() {
            @Override
            public boolean check(int x, int y) {
                if (tileIsPossible(x, y)) {
                    //на одной ячейке 2 персонажа
                    for (Sprite sprite : spritesBot) {
                        if (sprite.getX()==x && sprite.getY()==y)
                            return false;
                    }
                    for (Sprite sprite : spritesPlayer) {
                        if (sprite.getX()==x && sprite.getY()==y)
                            return false;
                    }

                    return true;
                }
                return false;
            }

        });

        if (mfp.findWay(startX, startY, endX, endY)) {
            return mfp.getWay();
        }

        return null;
    }
    public boolean possibleMove() {
        boolean isPossible = true;

        return isPossible;
    }
    public boolean tileIsPossible(int x, int y) {

        return ( map.getTile(x, y)==1&& possibleMove());
    }
    private void doOrder(){
        if(FIRST_ROUND){
            int flag=0;
            for(int i=0;i<6;i++) {
                Random random=new Random();
                int ran=random.nextInt(10);
                roundPlayer.add(createATB(i, ran, ran, spritesBot.get(i).getInit(),true,i));
                ran=random.nextInt(10);
                roundPlayer.add(createATB(i, ran, ran, spritesPlayer.get(i).getInit(),true,i+6));
            }
            int z=0;
            while (flag<7)
            {
                for (int i=0;i<roundPlayer.size();i++)
                {
                    if(roundPlayer.get(i).getIfFirstRound())
                    roundPlayer.get(i).firstStep();
                    else roundPlayer.get(i).step();
                    if(roundPlayer.get(i).getCurABT()>=100)
                    {
                        flag=flag+1;
                        roundPlayer.get(i).changeCurATB(-100);
                        roundPlayer.get(i).changeIfFirstRound();
                        showRoundPlayer.add(roundPlayer.get(i));

                    }
                }
            }
          //  Collections.sort(showRoundPlayer, snorderer);

         //   FIRST_ROUND=false;
            doStepMap();
        }
       /* else
        {
            showRoundPlayer.remove(0);
            int flag=0;
            while(flag<2)
            for (int i=0;i<roundPlayer.size();i++){
                roundPlayer.get(i).step();
                if(roundPlayer.get(i).getCurABT()>=100){
                    flag=flag+1;
                    roundPlayer.get(i).changeCurATB(-100);
                    showRoundPlayer.add(roundPlayer.get(i));

                }

            }
        }*/
    }

    // дальность хода
    private void doStepMap(){

        if(isWhoPlay()==0) {

            for (int y = 0; y < map.getMapHeight(); y++) {
                for (int x = 0; x < map.getMapWidth(); x++) {
                    if (spritesPlayer.get(isWhoGo()).getX() + spritesPlayer.get(isWhoGo()).getStep() > x && spritesPlayer.get(isWhoGo()).getX() - spritesPlayer.get(isWhoGo()).getStep() < x &&x<16&&
                            spritesPlayer.get(isWhoGo()).getY() + spritesPlayer.get(isWhoGo()).getStep() > y && spritesPlayer.get(isWhoGo()).getY() - spritesPlayer.get(isWhoGo()).getStep() < y) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.step);
                        tiles.get(y * 12 + x).changeWayPoint(bmp);

                    }
                    else {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                        tiles.get(y*12+x).changeWayPoint(bmp);
                    }
                }
            }
        }
        else
            for (int y = 0; y < map.getMapHeight(); y++) {
                for (int x = 0; x < map.getMapWidth(); x++) {
                    if (spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep() > x && spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() < x &&x<16&&
                            spritesBot.get(isWhoGo()).getY() + spritesBot.get(isWhoGo()).getStep() > y && spritesBot.get(isWhoGo()).getY() - spritesBot.get(isWhoGo()).getStep() < y){
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.step);
                    tiles.get(y * 12 + x).changeWayPoint(bmp);
                }
                   else {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                        tiles.get(y*12+x).changeWayPoint(bmp);
                    }
                }
            }
    }
private void delitInOrder(int flag){
    // первые 6-боты, с 6 по 12 - игрок
    for(int i=1;i<showRoundPlayer.size();i++){
        if( showRoundPlayer.get(i).getSizeInList()==flag+whoDefence)
            showRoundPlayer.remove(i);

    }
    for (int i=0;i<roundPlayer.size();i++)
    {
        if(roundPlayer.get(i).getSizeInList()==whoDefence+flag) {
            roundPlayer.remove(i);
            break;
        }
    }
}
   /* public static Comparator<ATB> snorderer = new Comparator<ATB>() {
        @Override
        public int compare(ATB o1, ATB o2) {
            return (o1.getCurABT() < o2.getCurABT()) ? -1 : 1;
        }
    };*/

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            showRoundPlayer.remove(0);
            int flag=0;
            while(flag<2)
                for (int i=0;i<roundPlayer.size();i++){
                    roundPlayer.get(i).step();
                    if(roundPlayer.get(i).getCurABT()>=100){
                        flag=flag+1;
                        roundPlayer.get(i).changeCurATB(-100);
                        showRoundPlayer.add(roundPlayer.get(i));

                    }

                }
            doStepMap();
         //   USE_STEP=true;

        }
        }
    // можно ли пойти в эту клетку
private boolean isGo(int isoX, int isoY)
{
    if(isWhoPlay()==0)
        if (spritesPlayer.get(isWhoGo()).getX() + spritesPlayer.get(isWhoGo()).getStep() > isoX && spritesPlayer.get(isWhoGo()).getX() - spritesPlayer.get(isWhoGo()).getStep() < isoX &&isoX<16&&
                spritesPlayer.get(isWhoGo()).getY() + spritesPlayer.get(isWhoGo()).getStep() > isoY && spritesPlayer.get(isWhoGo()).getY() - spritesPlayer.get(isWhoGo()).getStep() < isoY)
       return true;
            else return false;

    else
        if (spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep() > isoX && spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() < isoX &&isoX<16&&
                spritesBot.get(isWhoGo()).getY() + spritesBot.get(isWhoGo()).getStep() > isoY && spritesBot.get(isWhoGo()).getY() - spritesBot.get(isWhoGo()).getStep() < isoY)
        return true;
            else return false;

}
    private boolean isButton(int x, int y){
        if(x>934 &&y>548) {

            if(x>1122)
                if (y > 700)
                    //выход
                    System.exit(0);

            else {
                    // ждать
                    showRoundPlayer.add(3,showRoundPlayer.get(0));
                    showRoundPlayer.remove(0);
                    doStepMap();
                }
                else if(x<1085)
                         if(y>700) {
                         //конец игры
                            FINISH=true;
                        }

                         else {
                             //защита
                             showRoundPlayer.get(0).changeDefense(true);
                             if(isWhoPlay()==0)
                                 spritesPlayer.get(isWhoGo()).changeDefence(true);
                             else spritesBot.get(isWhoGo()).changeDefence(true);
                             showRoundPlayer.add(7,showRoundPlayer.get(0));
                             showRoundPlayer.remove(0);
                             doStepMap();

                         }

            return true;
        }
        else return false;

    }
}

