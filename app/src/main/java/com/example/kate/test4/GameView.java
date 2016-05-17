package com.example.kate.test4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class GameView extends SurfaceView {
    //игроки и боты
    private List<Sprite> spritesPlayer = new ArrayList<Sprite>();
    private List<Sprite> spritesBot = new ArrayList<Sprite>();
    //эффекты при ударах
    private List<Effect> spritesEffect = new ArrayList<Effect>();
    //АТВ-шкала
    private List<ATB> roundPlayer = new ArrayList<ATB>();
    private List<ATB> showRoundPlayer = new ArrayList<ATB>();
    //поле
    private List<Tile> tiles = new ArrayList<Tile>();
    private int whoGo = 0;
    private int whoDefence;
    private Map map;
    private boolean ATTACK = false;
    //ограничение по кол-ву кликов
    private long lastClick;
    //ограничение по времени работы алгоритма
    private long old;
    //для фитнесс-функции: (х,у) координаты игроков и ботов
    private double[] xForAlg;
    private double[] yForAlg;
    private int numberDraw = -1;
    private boolean isBot = false;
    private boolean playerIsAttack = false;
    private int playerWhoDefence = -1;
    private boolean FINISH = false;
    public boolean FIRST_ROUND_BOT = false;
    private SurfaceHolder holder;
    private boolean USE_STEP = false;
    private GameManager gameLoopThread;

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


        spritesBot.add(createSprite(R.drawable.bad6, 9, 2, 5, 3, 2, 4, 6, 3, 12, 20, 10, true, true));
        spritesBot.add(createSprite(R.drawable.bad9, 9, 4, 3, 5, 3, 5, 20, 4, 10, 14, 5, true, false));
        spritesBot.add(createSprite(R.drawable.bad8, 9, 6, 6, 6, 5, 7, 24, 3, 7, 9, 1, true, false));
        spritesBot.add(createSprite(R.drawable.bad10, 9, 8, 12, 9, 7, 7, 29, 2, 10, 5, 1, true, true));
        spritesBot.add(createSprite(R.drawable.bad11, 9, 10, 14, 14, 14, 19, 45, 3, 12, 3, 1, true, false));
        spritesBot.add(createSprite(R.drawable.bad12, 9, 12, 27, 20, 25, 35, 135, 3, 8, 2, 1, true, false));


        spritesPlayer.add(createSprite(R.drawable.bad1, 1, 2, 3, 3, 1, 4, 6, 2, 13, 16, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad2, 1, 4, 3, 4, 1, 4, 13, 3, 8, 15, 20, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad3, 1, 6, 4, 2, 4, 6, 15, 3, 13, 8, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad4, 1, 8, 6, 6, 6, 13, 32, 1, 9, 5, 6, false, true));
        spritesPlayer.add(createSprite(R.drawable.bad5, 1, 10, 18, 17, 10, 17, 66, 2, 15, 3, 2, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad7, 1, 12, 27, 23, 13, 31, 140, 2, 9, 2, 1, false, false));


    }

    private Sprite createSprite(int resource, int x, int y, int damage,
                                int defence, int minAttack, int maxAttack, int health, int step, int initiative, int morale, int number, boolean isBot, boolean shot) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(bmp, x, y, damage, defence, minAttack, maxAttack, health, step, initiative, morale, number, isBot, shot, false);
    }

    private Tile createTile(int resouce, int x, int y) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Tile(this, bmp, x, y);
    }

    private Effect createEffect(int resouce, int x, int y) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Effect(this, bmp, x, y);
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
        return new ATB(this, bmp, curATB, startATB, ini, ifFirstRound, sizeInList);
    }

    /**
     * Функция рисующая все спрайты и фон
     */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if (!FINISH) {

            if (USE_STEP) {
                for (Tile tile : tiles) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                    tile.changeWayPoint(bmp);
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
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
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
        if (FINISH) {
            if (isFinish() == 0) {
                Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.menu4);
                canvas.drawBitmap(bmp2, 500, 700, null);
                gameLoopThread.setRunning(false);

            } else {
                Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.menu3);
                canvas.drawBitmap(bmp2, 0, 0, null);
                gameLoopThread.setRunning(false);

            }
        }

    }

    /**
     * Обработка косания по экрану
     */
    public boolean onTouchEvent(MotionEvent e) {

        if (System.currentTimeMillis() - lastClick > 300) {

            // определение координат нажатия
            lastClick = System.currentTimeMillis();
            int _offX = (int) e.getX();
            int _y = (int) e.getY();
            //если меню
            if (isButton(_offX, _y))
                return false;

            int isoY = (_y / 36) + 1;
            int isoX = (_offX + ((isoY % 2 == 1) ? 60 : 0)) / 120;
            //можно ли в эту клетку ходить
            if (isGo(isoX, isoY))
                USE_STEP = true;
            else return false;
            //удар или нет
            // если удар - ближний бой или дальний
            int isAttack = 0;
            int shot = 0;

            if (isWhoPlay() == 0) {
                for (int i = 0; i < spritesBot.size(); i++) {
                    if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                        isAttack = 1;
                        isBot = true;
                        playerIsAttack = true;
                        playerWhoDefence = i;
                        whoDefence = i;

                    }
                }
                if (isAttack == 1) {
                    if (spritesPlayer.get(isWhoGo()).getShot())
                        shot = 1;

                    final int att = spritesPlayer.get(isWhoGo()).getAttack((int) spritesBot.get(playerWhoDefence).getX(),
                            (int) spritesBot.get(playerWhoDefence).getY(), spritesBot.get(playerWhoDefence).getDefence(), shot);

                    this.post(new Runnable() {
                        @Override
                        public void run() {

                            Toast toast = Toast.makeText(getContext(), "Будет убито " + Integer.toString((int)
                                    att / spritesBot.get(playerWhoDefence).getHealth())
                                    , Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
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

            // просто ход
            if (isAttack == 0)
                if (tileIsPossible(isoX, isoY)) {
                    numberDraw = -1;
                    whoGo = isWhoGo();
                    double x, y;
                    x = spritesPlayer.get(whoGo).getX();
                    y = spritesPlayer.get(whoGo).getY();
                    MapWay mapPath = makePath((int) x, (int) y, isoX, isoY);
                    spritesPlayer.get(whoGo).mapway = mapPath;
                    playerIsAttack = false;
                    playerWhoDefence = -1;
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
    protected void doAttack(int shot) {
        int damage;
        final int outlive;

        if (isWhoPlay() == 0) {
            final int flag = spritesBot.get(whoDefence).getNumber();
            damage = spritesPlayer.get(isWhoGo()).getAttack((int) spritesBot.get(whoDefence).getX(), (int) spritesBot.get(whoDefence).getY(),
                    spritesBot.get(whoDefence).getDefence(), shot);
            outlive = spritesBot.get(whoDefence).attack(damage);
            this.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getContext(), "Умерло " + Integer.toString(flag - outlive)
                            , Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.NO_GRAVITY, -i*120, -j);
                    toast.show();

                }
            });
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
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getContext(), "Один из противников умер"
                                    , Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });

            }


        } else {
            final int flag = spritesPlayer.get(whoDefence).getNumber();
            damage = spritesBot.get(isWhoGo()).getAttack((int) spritesPlayer.get(whoDefence).getX(), (int) spritesPlayer.get(whoDefence).getY(),
                    spritesPlayer.get(whoDefence).getDefence(), shot);
            outlive = spritesPlayer.get(whoDefence).attack(damage);
            this.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getContext(), "Умерло " + Integer.toString(flag - outlive)
                            , Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            if (outlive < 1) {
                delitInOrder(6);
                spritesPlayer.get(whoDefence).setDead(true);
                roundPlayer.get(whoDefence).setDead();
                isFinish();
                if (!FINISH)
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getContext(), "Один из ваших персонажей умер"
                                    , Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });


            }
        }


    }

    // какой перонаж из фракции ходит
    private int isWhoGo() {

        int flag = showRoundPlayer.get(0).getSizeInList();
        flag = flag - ((flag - 6 < 0) ? 0 : 6);
        return flag;
    }

    //ходит бот или игрок
    private int isWhoPlay() {

        int flag = showRoundPlayer.get(0).getSizeInList();
        if (showRoundPlayer.get(0).getDefence()) {
            showRoundPlayer.get(0).changeDefense(false);
            if (isWhoPlay() == 0)
                spritesPlayer.get(isWhoGo()).changeDefence(true);
            else spritesBot.get(isWhoGo()).changeDefence(true);
        }
        if (flag - 6 < 0)
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
                if (tileIsPossible(x, y)) {
                    //на одной ячейке 2 персонажа
                    for (Sprite sprite : spritesBot) {
                        if (sprite.getX() == x && sprite.getY() == y)
                            return false;
                    }
                    for (Sprite sprite : spritesPlayer) {
                        if (sprite.getX() == x && sprite.getY() == y)
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

    //возможно ли сделать шаг
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

    public boolean tileIsPossible(int x, int y) {

        return (map.getTile(x, y) == 1 && x < 10 && x > 0 && y < 16 && y > 0 && possibleMove(x, y));
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
        int z = 0;
        while (flag < 8) {
            for (int i = 0; i < roundPlayer.size(); i++) {
                if (roundPlayer.get(i).getIfFirstRound())
                    roundPlayer.get(i).firstStep();
                else roundPlayer.get(i).step();
                if (roundPlayer.get(i).getCurABT() >= 100) {
                    flag = flag + 1;
                    roundPlayer.get(i).changeCurATB(-100);
                    roundPlayer.get(i).changeIfFirstRound();
                    showRoundPlayer.add(roundPlayer.get(i));

                }
            }
        }

        if (isWhoPlay() == 0)
            doStepMap();
        else {
            FIRST_ROUND_BOT = true;
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
                        tiles.get(y * 12 + x).changeWayPoint(bmp);

                    } else {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                        tiles.get(y * 12 + x).changeWayPoint(bmp);
                    }
                }
            }
        } else
            for (int y = 0; y < map.getMapHeight(); y++) {
                for (int x = 0; x < map.getMapWidth(); x++) {
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pis);
                    tiles.get(y * 12 + x).changeWayPoint(bmp);
                }
            }
    }

    //удаление персонажа, если он умер
    private void delitInOrder(int flag) {
        // первые 6-боты, с 6 по 12 - игрок
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
            if (ATTACK) {
                doAttack(0 + ((isShot() == true) ? 1 : 0));
            }
            ATTACK = false;
            if (FIRST_ROUND_BOT) {
                doAlg();
                FIRST_ROUND_BOT = false;
                return;
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
                            roundPlayer.get(i).changeCurATB(-100);

                            showRoundPlayer.add(roundPlayer.get(i));

                        }
                    }

                }

            if (isWhoPlay() == 0)
                doStepMap();

            else {
                long old = System.currentTimeMillis();
                doStepMap();
                doAlg();
            }

        }
    }

    // можно ли пойти в эту клетку
    private boolean isGo(int isoX, int isoY) {

        if (isWhoPlay() == 0) {
            if (isShot() == true) {

                for (int i = 0; i < spritesBot.size(); i++) {
                    if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                        numberDraw = i;
                        return true;
                    }

                }
                for (int i = 0; i < spritesPlayer.size(); i++) {
                    if (isoX == (int) spritesPlayer.get(i).getX() && isoY == (int) spritesPlayer.get(i).getY() && !spritesPlayer.get(i).getDead()) {
                        numberDraw = i;
                        return false;
                    }
                }
                return false;
            }


            for (int i = 0; i < spritesPlayer.size(); i++) {
                if (isoX == (int) spritesPlayer.get(i).getX() && isoY == (int) spritesPlayer.get(i).getY() && !spritesPlayer.get(i).getDead()) {
                    numberDraw = i + spritesBot.size();
                    return false;
                }
            }
            if (spritesPlayer.get(isWhoGo()).getX() + spritesPlayer.get(isWhoGo()).getStep() > isoX && spritesPlayer.get(isWhoGo()).getX() - spritesPlayer.get(isWhoGo()).getStep() < isoX && isoX < 16 &&
                    spritesPlayer.get(isWhoGo()).getY() + spritesPlayer.get(isWhoGo()).getStep() > isoY && spritesPlayer.get(isWhoGo()).getY() - spritesPlayer.get(isWhoGo()).getStep() < isoY)
                return true;

            for (int i = 0; i < spritesBot.size(); i++) {
                if (isoX == (int) spritesBot.get(i).getX() && isoY == (int) spritesBot.get(i).getY() && !spritesBot.get(i).getDead()) {
                    numberDraw = i;
                    return false;
                }
            }
            return false;
        } else {


            if (spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep() > isoX && spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() < isoX && isoX < 16 &&
                    spritesBot.get(isWhoGo()).getY() + spritesBot.get(isWhoGo()).getStep() > isoY && spritesBot.get(isWhoGo()).getY() - spritesBot.get(isWhoGo()).getStep() < isoY)
                return true;
            else
                return false;
        }

    }

    //меню
    private boolean isButton(int x, int y) {
        if (x > 934 && y > 548) {
            //правая часть
            if (x > 1122) {
                if (y > 681) {
                    //количество персонажей
                    playerIsAttack = false;
                    playerWhoDefence = -1;
                    final int flag;
                    if (numberDraw > -1) {
                        if (numberDraw < spritesBot.size()) {
                            flag = spritesBot.get(numberDraw).getNumber();

                        } else {
                            flag = spritesPlayer.get(numberDraw - spritesBot.size()).getNumber();
                        }
                        this.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getContext(), "Персонажей " + Integer.toString(flag)
                                        , Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                }
                if (y < 645) {
                    // удар
                    numberDraw = -1;

                    if (playerIsAttack) {
                        int isoX = (int) spritesBot.get(playerWhoDefence).getX();
                        int isoY = (int) spritesBot.get(playerWhoDefence).getY();
                        // просто ход или, если ближний бой, то подойти к сопернику
                        if (playerIsAttack == true && !isShot()) {

                            whoGo = isWhoGo();
                            double X, Y;
                            X = spritesPlayer.get(whoGo).getX();
                            Y = spritesPlayer.get(whoGo).getY();
                            //если удар, то отходим на шаг назад
                            isoX = isoX - 1;
                            MapWay mapPath = makePath((int) X, (int) Y, isoX, isoY);
                            spritesPlayer.get(whoGo).mapway = mapPath;
                            //doAttack(0 + ((isShot() == true) ? 1 : 0));
                            ATTACK = true;
                            playerIsAttack = false;
                            playerWhoDefence = -1;
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
                        if (playerIsAttack == true && isShot()) {
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.f);
                            tiles.get((int) spritesBot.get(playerWhoDefence).getY() * 12 + (int) spritesBot.get(playerWhoDefence).getX()).changeWayPoint(bmp);

                            spritesEffect.add(createEffect(R.drawable.fire, (int) spritesBot.get(playerWhoDefence).getX(), (int) spritesBot.get(playerWhoDefence).getY()));
                            doAttack(0 + ((isShot() == true) ? 1 : 0));
                            playerIsAttack = false;
                            playerWhoDefence = -1;
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
                        playerWhoDefence = -1;
                        numberDraw = -1;
                        Timer mTimer = new Timer();
                        MyTimerTask myTimerTask = new MyTimerTask();
                        mTimer.schedule(myTimerTask, 10 * 8);
                    }
                } else {
                    //защита
                    showRoundPlayer.get(0).changeDefense(true);
                    if (isWhoPlay() == 0)
                        spritesPlayer.get(isWhoGo()).changeDefence(true);
                    else spritesBot.get(isWhoGo()).changeDefence(true);
                    if (showRoundPlayer.size() > 7)
                        showRoundPlayer.add(7, showRoundPlayer.get(0));
                    else
                        showRoundPlayer.add(showRoundPlayer.get(0));
                    playerIsAttack = false;
                    playerWhoDefence = -1;
                    numberDraw = -1;
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

        GeneticAlg ge = new GeneticAlg(spritesPlayer.size() + spritesBot.size() - 1, spritesPlayer, spritesBot, showRoundPlayer);
        int[] better = ge.run();
        xForAlg = null;
        yForAlg = null;
        if (System.currentTimeMillis() - old > 10000 && isWhoGo() != 0 && isWhoGo() != 3) {
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
        if (better[0] < spritesPlayer.size()) {//удар

            if (((int) spritesPlayer.get(better[0]).getX() > (int) spritesBot.get(isWhoGo()).getX() - spritesBot.get(isWhoGo()).getStep() &&
                    (int) spritesPlayer.get(better[0]).getX() < (int) spritesBot.get(isWhoGo()).getX() + spritesBot.get(isWhoGo()).getStep()) ||
                    isShot() == true)
                if (!spritesPlayer.get(better[0]).getDead()) {
                    if (isShot()) {
                        spritesEffect.add(createEffect(R.drawable.fire, (int) spritesPlayer.get(better[0]).getX(), (int) spritesPlayer.get(better[0]).getY()));

                    }
                    whoDefence = better[0];
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.f);
                    tiles.get((int) spritesPlayer.get(better[0]).getY() * 12 + (int) spritesPlayer.get(better[0]).getX()).changeWayPoint(bmp);

                    doMotion((int) spritesPlayer.get(better[0]).getX(), (int) spritesPlayer.get(better[0]).getY(), 1);
                    return;

                } else {

                    doAlg();
                }

        } else {

            if (better[0] == spritesBot.size() + spritesPlayer.size() + 1) {
                //защита

                showRoundPlayer.get(0).changeDefense(true);
                if (isWhoPlay() == 0)
                    spritesPlayer.get(isWhoGo()).changeDefence(true);
                else spritesBot.get(isWhoGo()).changeDefence(true);
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
                int x = 0;
                int y = 0;

                switch (better[0]) {
                    case 14: {
                        int i = 1;
                        while (i < spritesBot.get(isWhoGo()).getStep() + 1) {
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x + 1, (int) spritesBot.get(isWhoGo()).getY() + y))
                                x = x + 1;
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + 1 + y))
                                y = y + 1;
                            i++;
                        }
                        break;
                    }
                    case 15: {
                        int i = 1;
                        while (i < spritesBot.get(isWhoGo()).getStep() + 1) {
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() - 1 + x, (int) spritesBot.get(isWhoGo()).getY() + y))
                                x = x - 1;
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + y + 1))
                                y = y + 1;
                            i++;
                        }
                        break;

                    }
                    case 16: {
                        int i = 1;
                        while (i < spritesBot.get(isWhoGo()).getStep() + 1) {
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x + 1, (int) spritesBot.get(isWhoGo()).getY() + y))
                                x = x + 1;
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() - 1 + y))
                                y = y - 1;
                            i++;
                        }
                        break;
                    }
                    case 17: {
                        int i = 1;
                        while (i < spritesBot.get(isWhoGo()).getStep() + 1) {
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x - 1, (int) spritesBot.get(isWhoGo()).getY() + y))
                                x = x - 1;
                            if (tileIsPossible((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + y - 1))
                                y = y - 1;
                            i++;
                        }
                        break;
                    }

                }
                if (x == 0 && y == 0)
                    doAlg();
                else {
                    doMotion((int) spritesBot.get(isWhoGo()).getX() + x, (int) spritesBot.get(isWhoGo()).getY() + y, 0);
                    return;
                }
            }

            doAlg();

        }


    }


    //движение бота
    private void doMotion(int isoX, int isoY, int isAttack) {
        int shot = 0 + ((isShot() == true) ? 1 : 0);

        // просто ход или, если ближний бой, то подойти к сопернику
        if (isAttack == 0 || isAttack == 1 && shot != 1) {
            whoGo = isWhoGo();
            double x, y;
            if (isWhoPlay() == 0) {
                x = spritesPlayer.get(whoGo).getX();
                y = spritesPlayer.get(whoGo).getY();
            } else {
                x = spritesBot.get(whoGo).getX();
                y = spritesBot.get(whoGo).getY();
            }
            //если удар, то отходим на шаг назад
            if (isAttack == 1) {
                if (tileIsPossible(isoX + 1, isoY))
                    isoX = isoX + 1;
                else
                    isoY = isoY - 1;
            }
            MapWay mapPath = makePath((int) x, (int) y, isoX, isoY);

            spritesBot.get(whoGo).mapway = mapPath;
            if (isAttack == 1) {
                ATTACK = true;
                //   doAttack(shot);

            }

            Timer mTimer2 = new Timer();
            MyTimerTask myTimerTask = new MyTimerTask();
            if (shot == 0)
                mTimer2.schedule(myTimerTask, 10 * 100 * 4);
            else mTimer2.schedule(myTimerTask, 5 * 100 * 4);
        }

        // если выстрел
        if (isAttack == 1 && shot == 1) {

            doAttack(shot);
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

