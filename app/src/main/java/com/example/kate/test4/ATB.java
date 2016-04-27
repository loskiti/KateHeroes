package com.example.kate.test4;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Comparator;

/**
 * Created by Kate on 16.04.2016.
 */
public  class ATB  {
    //текущее положение
    private int curATB;
    private int ini;
    private int startATB;
    private int sizeInList;
    private boolean ifFirstRound;
    private GameView gameView;
    private Bitmap bmp;
    private boolean defence;
    public ATB(GameView gameView, Bitmap bmp, int curATB,int startATB, int ini, boolean ifFirstRound, int sizeInList)
    {
        this.gameView = gameView;
        this.bmp = bmp;
       this.curATB=curATB;
        this.startATB=startATB;
        this.ini=ini;
        this.ifFirstRound=ifFirstRound;
        this.sizeInList=sizeInList;
        defence=false;
    }
public void step(){
    curATB=curATB+ini;

}
    public void firstStep(){
        curATB=startATB+curATB+ini;

    }
    //-100 - новый ход ( в конец стека), -50- в середину стека
    public void changeCurATB(int flag){
        curATB=curATB+flag;
    }

    public int getCurABT(){
        return curATB;
    }
    public boolean getIfFirstRound()
    {
        return ifFirstRound;
    }
    public void changeIfFirstRound(){
        ifFirstRound=false;
    }
    public void onDraw (Canvas canvas, int x) {


        canvas.drawBitmap(bmp, x*120 , canvas.getHeight()-bmp.getHeight() , null);
    }
    public int getSizeInList(){
        return sizeInList;
    }
    public void changeDefense(boolean newDefence)
    {
        defence=newDefence;
    }
public boolean getDefence(){return defence;}
}
