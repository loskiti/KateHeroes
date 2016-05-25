package com.example.kate.test4.Base;

import android.graphics.Canvas;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GameManager extends Thread {

    private GameView view;
    private boolean running = false;

    public GameManager(GameView view) {
        this.view = view;

    }

    public void setRunning(boolean run) {
        running = run;
    }


    @Override
    public void run() {

        while (running) {
            Canvas c = null;

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

        }
    }
}

/*
public class GameManager{
    private View view;
        public GameManager(final GameView view){
            this.view=view;
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    Canvas c = null;
                    c = view.getHolder().lockCanvas();
                    synchronized (view.getHolder()) {
                        view.onDraw(c);
                    }
                }

            },
            0, 1, TimeUnit.SECONDS);

        }

}*/
