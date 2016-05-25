package com.example.kate.test4.Grafic;


import android.graphics.Bitmap;

import java.util.Random;

public class UnitSprite {
    /**
     * Рядов в спрайте = 4
     */
    private static final int BMP_ROWS = 4;
    /**
     * Колонок в спрайте = 3
     */
    private static final int BMP_COLUMNS = 3;
    private int width;
    private int height;
    protected Bitmap bmp;
    protected double x;
    protected double y;
    protected int minAttack;
    protected int maxAttack;
    protected int defence;
    protected int health;
    private int step;
    private int initiative;
    protected int outlive;
    protected int number;
    protected boolean isBot;
    private boolean shot;
    protected boolean dead;
    protected int newDefence = defence;

    public UnitSprite( Bitmap bmp, double x, double y,
                   int defence, int minAttack, int maxAttack, int health, int step, int initiative,  int number, boolean isBot, boolean shot, boolean dead) {
        this.bmp = bmp;
        //this.width = bmp.getWidth() / BMP_COLUMNS;
       // this.height = bmp.getHeight() / BMP_ROWS;
        this.x = x;
        this.y = y;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
        this.defence = defence;
        this.health = health;
        this.step = step;
        this.initiative = initiative;
        this.number = number;
        outlive = number * health;
        this.isBot = isBot;
        this.newDefence = defence;
        this.shot = shot;
        this.dead = dead;
    }
    public UnitSprite(  double x, double y,
                       int defence, int minAttack, int maxAttack, int health, int step, int initiative,  int number, boolean isBot, boolean shot, boolean dead) {

        //this.width = bmp.getWidth() / BMP_COLUMNS;
        // this.height = bmp.getHeight() / BMP_ROWS;
        this.x = x;
        this.y = y;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
        this.defence = defence;
        this.health = health;
        this.step = step;
        this.initiative = initiative;
        this.number = number;
        outlive = number * health;
        this.isBot = isBot;
        this.newDefence = defence;
        this.shot = shot;
        this.dead = dead;
    }
    //урон, который будет нанесен
    public int getAttack(int oppX, int oppY, int oppDefence, int flag) {

        Random random = new Random();

        int att = number * (minAttack + (maxAttack - minAttack) + random.nextInt(1)) *
                (int) (((minAttack < oppDefence) ? (1 / (1 + (oppDefence - minAttack) * 0.05)) : (1 + (minAttack - oppDefence) * 0.05)) + 1);

        //если стрелок
        if (flag != 0)
            att = (int) (att * ((Math.abs(oppX - x) < 6 && Math.abs(oppY - y) < 6) ? 1 : 0.5));
        assert (att>0);
        return att;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDefence() {
        return newDefence;
    }

    public int getInit() {
        return initiative;
    }

    public int getNumber() {
        return number;
    }

    public int getStep() {
        return step;
    }

    public int getHealth() {
        return health;
    }

    public boolean getShot() {
        return shot;
    }

    public boolean getDead() {
        return dead;
    }

    public void setDead(boolean flag) {
        dead = flag;
    }

    public void setDefence(boolean flag) {
        if (flag)
            newDefence = (int) (defence * 1.3);
        else
            newDefence = defence;
    }
    public UnitSprite createUnitSprite ( double x, double y,
    int defence, int minAttack, int maxAttack, int health, int step, int initiative,  int number, boolean isBot, boolean shot, boolean dead) {
        return new UnitSprite ( x, y, defence, minAttack, maxAttack,health,step, initiative, number,isBot, shot,dead);

    }

}
