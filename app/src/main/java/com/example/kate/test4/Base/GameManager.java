package com.example.kate.test4.Base;

import android.graphics.Canvas;

import com.example.kate.test4.Base.GameView;

public class GameManager extends Thread {
    /**
     * отрисовка 10 кадров в сек
     **/
    static final long FPS = 10;
    private GameView view;
    private boolean running = false;

    public GameManager(GameView view) {
        this.view = view;

    }

    public void setRunning(boolean run) {
        running = run;
    }

    /**
     * Действия, выполняемые в потоке
     */

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;

            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {
            }
        }
    }
}