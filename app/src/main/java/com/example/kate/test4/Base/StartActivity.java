
package com.example.kate.test4.Base;


import android.app.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kate.test4.R;



public class StartActivity extends Activity implements View.OnClickListener {

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 3000; //Время показа Splash картинки 3 секунд
    private ImageView splash;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", Thread.currentThread().getName());
        // если хотим, чтобы приложение постоянно имело портретную ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // если хотим, чтобы приложение было полноэкранным
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        Button startButton = (Button) findViewById(R.id.button1);
        startButton.setOnClickListener(this);
        Button exitButton = (Button) findViewById(R.id.button2);
        exitButton.setOnClickListener(this);
        splash = (ImageView) findViewById(R.id.splashscreen); //получаем индентификатор ImageView с Splash картинкой
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
        //музыка
       // startService(new Intent(this, MyService.class));

    }

    private Handler splashHandler = new Handler() { //создаем новый хэндлер
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    //убираем Splash картинку - меняем видимость
                    splash.setVisibility(View.GONE);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageResource(R.drawable.back);

                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            //переход на сюрфейс
            case R.id.button1: {
                setContentView(new GameView(this));

            }
            break;

            //выход
            case R.id.button2: {
                stopService(new Intent(this, MyService.class));
                finish();
            }
            break;

            default:
                break;
        }
    }
}



