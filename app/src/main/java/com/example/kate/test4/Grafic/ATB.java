package com.example.kate.test4.Grafic;

import android.graphics.Bitmap;
import android.graphics.Canvas;



public class ATB extends ATBUnit{

    public ATB( Bitmap bmp, int curATB, int startATB, int ini, boolean ifFirstRound, int sizeInList) {
        super(bmp,curATB,startATB,ini,ifFirstRound,sizeInList);
    }

    public void step() {
        curATB = curATB + ini;
    }

    public void firstStep() {
        curATB = startATB + curATB + ini;
    }


    public void onDraw(Canvas canvas, int x) {
        canvas.drawBitmap(bmp, x * 120, canvas.getHeight() - bmp.getHeight() - 50, null);
    }

}
