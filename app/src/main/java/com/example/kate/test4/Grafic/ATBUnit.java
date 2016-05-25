package com.example.kate.test4.Grafic;

import android.graphics.Bitmap;


public class ATBUnit {
//текущее положение
    protected int curATB;
    //инициатива
    protected int ini;
    //начальное положение
    protected int startATB;
    //первые боты, потом игроки
    private int sizeInList;
    private boolean ifFirstRound;
    protected Bitmap bmp;
    private boolean defence;
    private boolean dead;
    public ATBUnit( Bitmap bmp, int curATB, int startATB, int ini, boolean ifFirstRound, int sizeInList) {
        this.bmp = bmp;
        this.curATB = curATB;
        this.startATB = startATB;
        this.ini = ini;
        this.ifFirstRound = ifFirstRound;
        this.sizeInList = sizeInList;
        defence = false;
        dead = false;
    }
    public int getCurABT() {
        return curATB;
    }

    public boolean getIfFirstRound() {
        return ifFirstRound;
    }

    public void setIfFirstRound() {
        ifFirstRound = false;
    }
    public int getSizeInList() {
        return sizeInList;
    }

    public void setDefense(boolean newDefence) {
        defence = newDefence;
    }

    public boolean getDefence() {
        return defence;
    }

    public boolean getDead() {
        return dead;
    }

    public void setDead() {
        dead = true;
    }
    //-100 - новый ход ( в конец стека), -50- в середину стека
    public void setCurATB(int flag) {
        curATB = curATB + flag;
    }
}
