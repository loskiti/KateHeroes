package com.example.kate.test4.Base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.kate.test4.*;
import com.example.kate.test4.Grafic.ATB;
import com.example.kate.test4.Grafic.Effect;
import com.example.kate.test4.Grafic.Sprite;
import com.example.kate.test4.Grafic.Tile;
import com.example.kate.test4.Map.IMapCheckPoint;
import com.example.kate.test4.Map.Map;
import com.example.kate.test4.Map.MapWay;
import com.example.kate.test4.Map.MapWayFind;

import java.util.*;
import java.util.ArrayList;
import java.util.List;



public class GameView extends SurfaceView {
    //игроки и боты
    private List<Sprite> spritesPlayer = new ArrayList<>();
    private List<Sprite> spritesBot = new ArrayList<>();
    //эффекты при ударах
    private List<Effect> spritesEffect = new ArrayList<>();
    //АТВ-шкала
    private List<ATB> roundPlayer = new ArrayList<>();
    private List<ATB> showRoundPlayer = new ArrayList<>();
    //поле
    private List<Tile> tiles = new ArrayList<>();
    private Map map;
    private GameManager gameLoopThread;
    //ограничение по кол-ву кликов
    private long lastClick;
    //ограничение по времени работы алгоритма
    private long old;
    private boolean FINISH = false;
    private boolean playerIsAttack = false;
    private int whoDefence;

   // public boolean FIRST_ROUND_BOT = false;
    private boolean USE_STEP = false;
   // private boolean ATTACK = false;
    //1-первым играет бот (задержка для отрисовки), 3-задержка для атаки
    private int doTimer;


    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameManager(this);
        SurfaceHolder holder = getHolder();
        map = new Map(context);
          /*Рисуем все наши объекты */
        holder.addCallback(new SurfaceHolder.Callback() {
            //*** Уничтожение области рисования
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                       gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            // Создание области рисования
            public void surfaceCreated(SurfaceHolder holder) {


                for (int y = 0; y < map.getMapHeight(); y++) {
                    for (int x = 0; x < map.getMapWidth(); x++) {
                        int id = map.getMapTile(x, y);
                        tiles.add(createTile(map.getTileName(id), x, y));
                    }
                }
                createSprites();
                doOrder();

                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            // Изменение области рисования
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

    }

    private void createSprites() {


        spritesBot.add(createSprite(R.drawable.bad6, 9, 2, 3, 2, 4, 6, 3, 12, 10, true, true));
        spritesBot.add(createSprite(R.drawable.bad9, 9, 4, 5, 3, 5, 20, 4, 10, 5, true, false));
        spritesBot.add(createSprite(R.drawable.bad8, 9, 6, 6, 5, 7, 24, 3, 7, 1, true, false));
        spritesBot.add(createSprite(R.drawable.bad10, 9, 8, 9, 7, 7, 29, 2, 10, 1, true, true));
        spritesBot.add(createSprite(R.drawable.bad11, 9, 10, 14, 14, 19, 45, 3, 12, 1, true, false));
        spritesBot.add(createSprite(R.drawable.bad12, 9, 12, 20, 25, 35, 135, 3, 8, 1, true, false));


        spritesPlayer.add(createSprite(R.drawable.bad1, 1, 2, 3, 1, 4, 6, 2, 13, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad2, 1, 4, 4, 1, 4, 13, 3, 8, 20, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad3, 1, 6, 2, 4, 6, 15, 3, 13, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad4, 1, 8, 6, 6, 13, 32, 1, 9, 6, false, true));
        spritesPlayer.add(createSprite(R.drawable.bad5, 1, 10, 17, 10, 17, 66, 2, 15, 2, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad7, 1, 12, 23, 13, 31, 140, 2, 9, 1, false, false));


    }

    private Sprite createSprite(int resource, int x, int y,
                                int defence, int minAttack, int maxAttack, int health, int step, int initiative, int number, boolean isBot, boolean shot) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(bmp, x, y, defence, minAttack, maxAttack, health, step, initiative, number, isBot, shot, false);
    }

    private Tile createTile(int resouce, int x, int y) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Tile(bmp, x, y);
    }

    private Effect createEffect(int resouce, int x, int y) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Effect(bmp, x, y);
    }

    private ATB createATB(int id, int curATB, int startATB, int ini, boolean ifFirstRound, int sizeInList) {
        int resouce;

        switch (id) {
            case 0: {
                resouce = R.drawable.b6;
                break;
            }
            case 1: {
                resouce = R.drawable.b9;
                break;
            }
            case 2: {
                resouce = R.drawable.b8;
                break;
            }
            case 3: {
                resouce = R.drawable.b10;
                break;
            }
            case 4: {
                resouce = R.drawable.b11;
                break;
            }
            case 5: {
                resouce = R.drawable.b12;
                break;
            }
            case 6: {
                resouce = R.drawable.b1;
                break;
            }
            case 7: {
                resouce = R.drawable.b2;
                break;
            }
            case 8: {
                resouce = R.drawable.b3;
                break;
            }
            case 9: {
                resouce = R.drawable.b4;
                break;
            }
            case 10: {
                resouce = R.drawable.b5;
                break;
            }

            default: {
                resouce = R.drawable.b7;
                break;
            }
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new ATB(bmp, curATB, startATB, ini, ifFirstRound, sizeInList);
    }

    /**
     * Функция рисующая все спрайты и фон
     */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if (FINISH) {
            if (isFinish() == 0) {
                Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.menu4);
                canvas.drawBitmap(bmp2, 500, 700, null);
                //gameLoopThread.setRunning(false);
                return;
            } else {
                Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.menu3);
                canvas.drawBitmap(bmp2, 0, 0, null);
               // gameLoopThread.setRunning(false);
                return;

            }

        }

        if (USE_STEP) {
            for (Tile tile : tiles) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                tile.setWayPoint(bmp);
            }
            USE_STEP = false;
        }

        for (Tile tile : tiles) {
            tile.onDraw(canvas);

        }

        for (Sprite sprite : spritesBot) {
            sprite.onDraw(canvas);

        }
        for (final Sprite sprite : spritesPlayer) {
            sprite.onDraw(canvas);

        }
        if (spritesEffect.size() != 0)
            spritesEffect.get(spritesEffect.size() - 1).onDraw(canvas);
        for (int i = 0; i < showRoundPlayer.size(); i++) {
            if (i < 8)
                showRoundPlayer.get(i).onDraw(canvas, i);
        }
        //меню
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.menu2);
        canvas.drawBitmap(bmp1, canvas.getWidth() - bmp1.getWidth(), canvas.getHeight() - bmp1.getHeight(), null);


    }

    /**
     * Обработка косания по экрану
     */
    public boolean onTouchEvent(MotionEvent e) {

        if (System.currentTimeMillis() - lastClick > 300) {

            lastClick = System.currentTimeMillis();

            //если меню
            if (isButton((int) e.getX(), (int) e.getY()))
                return false;
            // определение координат нажатия
            int isoY = ((int) e.getY() / 36) + 1;
            int isoX = ((int) e.getX() + ((isoY % 2 == 1) ? 60 : 0)) / 120;
            assert (isoX<10 && isoX>0);
            assert (isoY<16 && isoY>0);
            //можно ли в эту клетку ходить или кол-во персонажей
            if (isGo(isoX, isoY))
                USE_STEP = true;
            else return false;
            //удар или нет
            if (isWhoPlay() == 0) {
                for (int i = 0; i < spritesBot.size(); i++) {
                    if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                        playerIsAttack = true;
                        whoDefence = i;
                        doSMS("Будет убито " + Integer.toString(spritesPlayer.get(isWhoGo()).getAttack((int) spritesBot.get(whoDefence).getX(),
                                (int) spritesBot.get(whoDefence).getY(), spritesBot.get(whoDefence).getDefence(), (((spritesPlayer.get(isWhoGo()).getShot()) ? 1 : 0))) / spritesBot.get(whoDefence).getHealth()));

                        break;

                    }
                }

            }
            // просто ход
            if (!playerIsAttack)
                if (tileIsPossible(isoX, isoY)) {
                    spritesPlayer.get(isWhoGo()).mapway = makePath((int) spritesPlayer.get(isWhoGo()).getX(), (int) spritesPlayer.get(isWhoGo()).getY(), isoX, isoY);
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            Timer mTimer = new Timer();
                            MyTimerTask myTimerTask = new MyTimerTask();
                            mTimer.schedule(myTimerTask, 5 * 100 * 5);
                        }
                    });

                }


        }

        return true;
    }

    //удар
    protected void doAttack() {
        final int outlive;
        final int flag;
        if (isWhoPlay() == 0) {
            flag = spritesBot.get(whoDefence).getNumber();
            outlive = spritesBot.get(whoDefence).attack(spritesPlayer.get(isWhoGo()).getAttack((int) spritesBot.get(whoDefence).getX(), (int) spritesBot.get(whoDefence).getY(),
                    spritesBot.get(whoDefence).getDefence(), ((isShot()) ? 1 : 0)));
            doSMS("Умерло " + Integer.toString(flag - outlive));

            if (outlive < 1) {
                delitInOrder(0);
                spritesBot.get(whoDefence).setDead(true);
                int flagRound = 0;
                for (int i = 0; i < roundPlayer.size(); i++) {
                    if (roundPlayer.get(i).getSizeInList() < 6)
                        flagRound++;
                }
                roundPlayer.get(whoDefence + flagRound).setDead();
                isFinish();
                if (!FINISH)
                    doSMS("Один из противников умер");


            }
            //whoDefence = -1;


        } else {
            flag = spritesPlayer.get(whoDefence).getNumber();
            outlive = spritesPlayer.get(whoDefence).attack(spritesBot.get(isWhoGo()).getAttack((int) spritesPlayer.get(whoDefence).getX(), (int) spritesPlayer.get(whoDefence).getY(),
                    spritesPlayer.get(whoDefence).getDefence(), ((isShot()) ? 1 : 0)));
            doSMS("Умерло " + Integer.toString(flag - outlive));

            if (outlive < 1) {
                delitInOrder(6);
                spritesPlayer.get(whoDefence).setDead(true);
                roundPlayer.get(whoDefence).setDead();
                isFinish();
                if (!FINISH)
                    doSMS("Один из ваших персонажей умер");


            }

        }
        whoDefence = -1;

    }

    // какой перонаж из фракции ходит
    private int isWhoGo() {
        assert (showRoundPlayer.get(0).getSizeInList() - ((showRoundPlayer.get(0).getSizeInList() - 6 < 0) ? 0 : 6)<6);
        return (showRoundPlayer.get(0).getSizeInList() - ((showRoundPlayer.get(0).getSizeInList() - 6 < 0) ? 0 : 6));

    }

    //ходит бот или игрок
    private int isWhoPlay() {
        //изменение защиты на 30%
        if (showRoundPlayer.get(0).getDefence()) {
            showRoundPlayer.get(0).setDefense(false);
            if (isWhoPlay() == 0)
                spritesPlayer.get(isWhoGo()).setDefence(true);
            else spritesBot.get(isWhoGo()).setDefence(true);
        }
        if (showRoundPlayer.get(0).getSizeInList() - 6 < 0)
            return 1;
        else
            return 0;
    }

    //хождение
    public MapWay makePath(int startX, int startY, int endX, int endY) {
        MapWayFind mfp = new MapWayFind();
        mfp.setcheckPoint(new IMapCheckPoint() {
            @Override
            public boolean check(int x, int y) {
                return tileIsPossible(x, y);
            }

        });

        if (mfp.findWay(startX, startY, endX, endY)) {
            return mfp.getWay();
        }

        return null;
    }


    //на одной ячейке 2 персонажа
    public boolean possibleMove(int x, int y) {
        for (Sprite sprite : spritesBot) {
            if (sprite.getX() == x && sprite.getY() == y && !sprite.getDead())
                return false;
        }
        for (Sprite sprite : spritesPlayer) {
            if (sprite.getX() == x && sprite.getY() == y && !sprite.getDead())
                return false;
        }

        return true;
    }

    //возможно ли сделать шаг
    public boolean tileIsPossible(int x, int y) {

        return (x < 10 && x > 0 && y < 16 && y > 0 && possibleMove(x, y));
    }

    //обновить АТВ-шкалу
    private void doOrder() {

        int flag = 0;
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int ran = random.nextInt(10);
            roundPlayer.add(createATB(i, ran, ran, spritesBot.get(i).getInit(), true, i));
            ran = random.nextInt(10);
            roundPlayer.add(createATB(i + 6, ran, ran, spritesPlayer.get(i).getInit(), true, i + 6));
        }
        while (flag < 8) {
            for (int i = 0; i < roundPlayer.size(); i++) {
                if (roundPlayer.get(i).getIfFirstRound())
                    roundPlayer.get(i).firstStep();
                else roundPlayer.get(i).step();
                if (roundPlayer.get(i).getCurABT() >= 100) {
                    flag = flag + 1;
                    roundPlayer.get(i).setCurATB(-100);
                    roundPlayer.get(i).setIfFirstRound();
                    showRoundPlayer.add(roundPlayer.get(i));

                }
            }
        }

        if (isWhoPlay() == 0)
            doStepMap();
        else {
            doTimer=1;
            //FIRST_ROUND_BOT = true;
            this.post(new Runnable() {
                @Override
                public void run() {
                    Timer mTimer = new Timer();
                    MyTimerTask myTimerTask = new MyTimerTask();
                    mTimer.schedule(myTimerTask, 5000);
                }
            });
        }

    }

    // дальность хода
    private void doStepMap() {

        if (isWhoPlay() == 0) {

            for (int y = 0; y < map.getMapHeight(); y++) {
                for (int x = 0; x < map.getMapWidth(); x++) {
                    if (spritesPlayer.get(isWhoGo()).getX() + spritesPlayer.get(isWhoGo()).getStep() > x && spritesPlayer.get(isWhoGo()).getX() - spritesPlayer.get(isWhoGo()).getStep() < x && x < 16 &&
                            spritesPlayer.get(isWhoGo()).getY() + spritesPlayer.get(isWhoGo()).getStep() > y && spritesPlayer.get(isWhoGo()).getY() - spritesPlayer.get(isWhoGo()).getStep() < y) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.step);
                        tiles.get(y * 12 + x).setWayPoint(bmp);

                    } else {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                        tiles.get(y * 12 + x).setWayPoint(bmp);
                    }
                }
            }
        } else
            for (int y = 0; y < map.getMapHeight(); y++) {
                for (int x = 0; x < map.getMapWidth(); x++) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                    tiles.get(y * 12 + x).setWayPoint(bmp);
                }
            }
    }

    //удаление персонажа, если он умер
    private void delitInOrder(int flag) {
        // первые 6-боты, с 6 по 12 - игрок
        assert (flag+whoDefence<12);
        for (int i = 1; i < showRoundPlayer.size(); i++) {
            if (showRoundPlayer.get(i).getSizeInList() == flag + whoDefence)
                showRoundPlayer.remove(i);

        }
        for (int i = 0; i < roundPlayer.size(); i++) {
            if (roundPlayer.get(i).getSizeInList() == whoDefence + flag) {
                roundPlayer.remove(i);
                break;
            }
        }
    }

    //слеующий ход-расчет АТВ-шкалы
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
           /* if (ATTACK) {
                doAttack();
            }
            ATTACK = false;*/
           /* if (FIRST_ROUND_BOT) {
                doAlg();
                FIRST_ROUND_BOT = false;
                return;
            }*/
            switch (doTimer){
                case 1:{
                    doAlg();
                    doTimer=0;
                    return;
                }
                case 3:{
                    doAttack();
                    doTimer=0;
                    break;
                }
            }

            showRoundPlayer.remove(0);
            int flag = 0;
            if (spritesEffect.size() != 0)
                if (spritesEffect.get(0).getUSE_EFF())
                    spritesEffect.clear();
            while (flag < 2)
                for (int i = 0; i < roundPlayer.size(); i++) {
                    if (!roundPlayer.get(i).getDead()) {
                        roundPlayer.get(i).step();
                        if (roundPlayer.get(i).getCurABT() >= 100) {
                            flag = flag + 1;
                            roundPlayer.get(i).setCurATB(-100);

                            showRoundPlayer.add(roundPlayer.get(i));

                        }
                    }

                }

            if (isWhoPlay() == 0)
                doStepMap();

            else {
                old = System.currentTimeMillis();
                doStepMap();
                doAlg();
            }

        }
    }

    // можно ли пойти в эту клетку или кол-во персонажей
    private boolean isGo(int isoX, int isoY) {
        if (isWhoPlay() == 0) {
            if (isShot()) {

                for (int i = 0; i < spritesBot.size(); i++) {
                    if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                        return true;
                    }

                }
                for (int i = 0; i < spritesPlayer.size(); i++) {
                    if (isoX == (int) spritesPlayer.get(i).getX() && isoY == (int) spritesPlayer.get(i).getY() && !spritesPlayer.get(i).getDead()) {
                        playersNumber(i);
                        return false;
                    }
                }
                return false;
            }


            for (int i = 0; i < spritesPlayer.size(); i++) {
                if (isoX == (int) spritesPlayer.get(i).getX() && isoY == (int) spritesPlayer.get(i).getY() && !spritesPlayer.get(i).getDead()) {
                    playersNumber(i + spritesBot.size());
                    return false;
                }
            }
            if (spritesPlayer.get(isWhoGo()).getX() + spritesPlayer.get(isWhoGo()).getStep() > isoX && spritesPlayer.get(isWhoGo()).getX() - spritesPlayer.get(isWhoGo()).getStep() < isoX && isoX < 16 &&
                    spritesPlayer.get(isWhoGo()).getY() + spritesPlayer.get(isWhoGo()).getStep() > isoY && spritesPlayer.get(isWhoGo()).getY() - spritesPlayer.get(isWhoGo()).getStep() < isoY)
                return true;

            for (int i = 0; i < spritesBot.size(); i++) {
                if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                    playersNumber(i);
                    return false;
                }
            }
            return false;
        } else {

            return spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep() > isoX && spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() < isoX && isoX < 16 &&
                    spritesBot.get(isWhoGo()).getY() + spritesBot.get(isWhoGo()).getStep() > isoY && spritesBot.get(isWhoGo()).getY() - spritesBot.get(isWhoGo()).getStep() < isoY;
        }

    }

    //кол-во персонажей
    private void playersNumber(int numberDraw) {

        final int flag;

        if (numberDraw < spritesBot.size()) {
            flag = spritesBot.get(numberDraw).getNumber();

        } else {
            flag = spritesPlayer.get(numberDraw - spritesBot.size()).getNumber();
        }
        doSMS("Персонажей " + Integer.toString(flag));
        whoDefence = -1;

    }

    private void doSMS(final String sms) {
        this.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getContext(), sms
                        , Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    //меню
    private boolean isButton(int x, int y) {
        if (x > 934 && y > 548) {
            //правая часть
            if (x > 1122) {
                if (y > 681) {
                    //выход
                    System.runFinalizersOnExit(true);
                    System.exit(0);

                }
                if (y < 645) {
                    // удар

                    if (playerIsAttack) {
                        int isoX = (int) spritesBot.get(whoDefence).getX();
                        int isoY = (int) spritesBot.get(whoDefence).getY();
                        // просто ход или, если ближний бой, то подойти к сопернику
                        if (playerIsAttack && !isShot()) {

                            double X, Y;
                            X = spritesPlayer.get(isWhoGo()).getX();
                            Y = spritesPlayer.get(isWhoGo()).getY();
                            //если удар, то отходим на шаг назад
                            isoX = isoX - 1;
                            spritesPlayer.get(isWhoGo()).mapway = makePath((int) X, (int) Y, isoX, isoY);
                            //doAttack(0 + ((isShot() == true) ? 1 : 0));
                            //ATTACK = true;
                            doTimer=3;
                            playerIsAttack = false;
                            this.post(new Runnable() {
                                @Override
                                public void run() {
                                    Timer mTimer = new Timer();
                                    MyTimerTask myTimerTask = new MyTimerTask();
                                    mTimer.schedule(myTimerTask, 5 * 100 * 5);
                                }
                            });

                        }

                        // если выстрел
                        if (playerIsAttack && isShot()) {
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.f);
                            tiles.get((int) spritesBot.get(whoDefence).getY() * 12 + (int) spritesBot.get(whoDefence).getX()).setWayPoint(bmp);

                            spritesEffect.add(createEffect(R.drawable.fire, (int) spritesBot.get(whoDefence).getX(), (int) spritesBot.get(whoDefence).getY()));
                            doAttack();
                            playerIsAttack = false;
                            this.post(new Runnable() {
                                @Override
                                public void run() {
                                    Timer mTimer = new Timer();
                                    MyTimerTask myTimerTask = new MyTimerTask();
                                    mTimer.schedule(myTimerTask, 100 * 4);
                                }
                            });

                        }
                    }
                }

            } else {
                //левая часть
                if (x < 1015) {
                    if (y > 681) {
                        //конец игры
                        FINISH = true;
                    }
                    if (y < 645) {
                        //ожидание
                        if (showRoundPlayer.size() > 3)
                            showRoundPlayer.add(3, showRoundPlayer.get(0));
                        else
                            showRoundPlayer.add(showRoundPlayer.get(0));
                        playerIsAttack = false;
                        whoDefence = -1;
                        Timer mTimer = new Timer();
                        MyTimerTask myTimerTask = new MyTimerTask();
                        mTimer.schedule(myTimerTask, 10 * 8);
                    }
                } else {
                    //защита
                    showRoundPlayer.get(0).setDefense(true);
                    if (isWhoPlay() == 0)
                        spritesPlayer.get(isWhoGo()).setDefence(true);
                    else spritesBot.get(isWhoGo()).setDefence(true);
                    if (showRoundPlayer.size() > 7)
                        showRoundPlayer.add(7, showRoundPlayer.get(0));
                    else
                        showRoundPlayer.add(showRoundPlayer.get(0));
                    playerIsAttack = false;
                    whoDefence = -1;
                    Timer mTimer = new Timer();
                    MyTimerTask myTimerTask = new MyTimerTask();
                    mTimer.schedule(myTimerTask, 10 * 8);

                }

                return true;
            }
        }
        return false;


    }

    //определение на выстрел
    private boolean isShot() {
        if (isWhoPlay() == 0) {
            if (spritesPlayer.get(isWhoGo()).getShot())
                return true;
        } else {

            if (spritesBot.get(isWhoGo()).getShot())
                return true;
        }

        return false;
    }

    //ход моба
    private void doAlg() {

        GeneticAlg ge = new GeneticAlg( spritesPlayer, spritesBot, showRoundPlayer);
        int[] better = ge.run();


        if (System.currentTimeMillis() - old > 100 && isWhoGo() != 0 && isWhoGo() != 3) {
            Random random = new Random();
            better[0] = -1;
            for (int i = 0; i < spritesPlayer.size(); i++) {
                if (!spritesPlayer.get(i).getDead()) {
                    int isoX = (int) spritesPlayer.get(i).getX();
                    int isoY = (int) spritesPlayer.get(i).getY();
                    if (isGo(isoX, isoY)) {
                        better[0] = i;
                        break;
                    }
                }


            }
            if (better[0] == -1)
                better[0] = random.nextInt(3) + 14;
            old = 0;
        }
        assert (better[0]<spritesBot.size()+spritesPlayer.size()+5);
        if (better[0] < spritesPlayer.size()) {//удар

            if (((int) spritesPlayer.get(better[0]).getX() > (int) spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() &&
                    (int) spritesPlayer.get(better[0]).getX() < (int) spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep()) ||
                    isShot())
                if (!spritesPlayer.get(better[0]).getDead()) {
                    if (isShot()) {
                        spritesEffect.add(createEffect(R.drawable.fire, (int) spritesPlayer.get(better[0]).getX(), (int) spritesPlayer.get(better[0]).getY()));

                    }
                    whoDefence = better[0];
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.f);
                    tiles.get((int) spritesPlayer.get(better[0]).getY() * 12 + (int) spritesPlayer.get(better[0]).getX()).setWayPoint(bmp);

                    doMotion((int) spritesPlayer.get(better[0]).getX(), (int) spritesPlayer.get(better[0]).getY(), 1);

                } else {

                    doAlg();
                }

        } else {

            if (better[0] == spritesBot.size() + spritesPlayer.size() + 1) {
                //защита

                showRoundPlayer.get(0).setDefense(true);
                if (isWhoPlay() == 0)
                    spritesPlayer.get(isWhoGo()).setDefence(true);
                else spritesBot.get(isWhoGo()).setDefence(true);
                if (showRoundPlayer.size() > 7)
                    showRoundPlayer.add(7, showRoundPlayer.get(0));
                else
                    showRoundPlayer.add((showRoundPlayer.get(0)));
                Timer mTimer = new Timer();
                MyTimerTask myTimerTask = new MyTimerTask();
                mTimer.schedule(myTimerTask, 10 * 50);
                return;
            }
            if (better[0] == spritesBot.size() + spritesPlayer.size()) {//ожидание
                if (showRoundPlayer.size() > 3)
                    showRoundPlayer.add(3, showRoundPlayer.get(0));
                else
                    showRoundPlayer.add((showRoundPlayer.get(0)));
                Timer mTimer = new Timer();
                MyTimerTask myTimerTask = new MyTimerTask();
                mTimer.schedule(myTimerTask, 50 * 100);
                return;
            }
            if (better[0] > spritesBot.size() + spritesPlayer.size() + 1 &&
                    better[0] < spritesBot.size() + spritesPlayer.size() + 6) {
                //ход
                boolean successful = false;
                switch (better[0]) {
                    case 14: {
                        successful = makePathForAlf(1, 1);
                        break;
                    }
                    case 15: {
                        successful = makePathForAlf(-1, 1);
                        break;

                    }
                    case 16: {
                        successful = makePathForAlf(1, -1);
                        break;
                    }
                    case 17: {

                        successful = makePathForAlf(-1, -1);
                        break;
                    }

                }

                if (!successful)
                    doAlg();
                else
                    return;
            }

            doAlg();

        }


    }

    private boolean makePathForAlf(int x1, int y1) {
        int i = 0;
        int x = 0;
        int y = 0;
        while (i < spritesBot.get(isWhoGo()).getStep() ) {
            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x + x1, (int) spritesBot.get(isWhoGo()).getY() + y))
                x = x + x1;
            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + y + y1))
                y = y + y1;
            i++;
        }
         if (x == 0 && y == 0)
            return false;
        else {
            doMotion((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + y, 0);
            return true;
        }
        }




    //движение бота
    private void doMotion(int isoX, int isoY, int isAttack) {
        // просто ход или, если ближний бой, то подойти к сопернику
        if (isAttack == 0 || isAttack == 1 && !isShot()) {

            //если удар, то отходим на шаг назад
            if (isAttack == 1) {
                if (tileIsPossible(isoX + 1, isoY))
                    isoX = isoX + 1;

            }

            spritesBot.get(isWhoGo()).mapway = makePath((int) spritesBot.get(isWhoGo()).getX(), (int) spritesBot.get(isWhoGo()).getY(), isoX, isoY);
            if (isAttack == 1) {
                doTimer=3;
               // ATTACK = true;
                //   doAttack(shot);

            }

            Timer mTimer2 = new Timer();
            MyTimerTask myTimerTask = new MyTimerTask();
            mTimer2.schedule(myTimerTask, 10 * 100 * 4);

        }

        // если выстрел
        if (isAttack == 1 && isShot()) {

            doAttack();
            this.post(new Runnable() {
                @Override
                public void run() {
                    Timer mTimer = new Timer();
                    MyTimerTask myTimerTask = new MyTimerTask();
                    mTimer.schedule(myTimerTask, 100 * 8);
                }
            });


        }


    }

    private int isFinish() {
        int flagDead = 0;
        for (int i = 0; i < spritesBot.size(); i++) {
            if (spritesBot.get(i).getDead())
                flagDead++;
        }
        if (flagDead == 6) {

            FINISH = true;
            return 1;
        } else flagDead = 0;
        for (int i = 0; i < spritesPlayer.size(); i++) {
            if (spritesPlayer.get(i).getDead())
                flagDead++;
        }
        if (flagDead == 6) {
            FINISH = true;
            return 0;

        }
        return -1;

    }



}

