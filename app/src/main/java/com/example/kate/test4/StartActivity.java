package com.example.kate.test4;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Kate on 19.04.2016.
 */
public class StartActivity extends Activity implements View.OnClickListener {

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 3000; //Время показа Splash картинки 3 секунд
    private ImageView splash;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            // если хотим, чтобы приложение постоянно имело портретную ориентацию
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // если хотим, чтобы приложение было полноэкранным
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // и без заголовка
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.layout);
          //  setContentView(new GameView(this));

            Button startButton = (Button)findViewById(R.id.button1);
            startButton.setOnClickListener(this);

            Button exitButton = (Button)findViewById(R.id.button2);
            exitButton.setOnClickListener(this);
            splash = (ImageView) findViewById(R.id.splashscreen); //получаем индентификатор ImageView с Splash картинкой
            Message msg = new Message();
            msg.what = STOPSPLASH;
            splashHandler.sendMessageDelayed(msg, SPLASHTIME);

         //   startService(new Intent(this, MyService.class));

        }
    private Handler splashHandler = new Handler() { //создаем новый хэндлер
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    //убираем Splash картинку - меняем видимость
                    splash.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

        /** Обработка нажатия кнопок */
        public void onClick(View v) {
            switch (v.getId()) {
                //переход на сюрфейс
                case R.id.button1: {
                 //   Intent intent = new Intent();
                  //  intent.setClass(this, MainActivity.class);
                //    Intent intent = new Intent(this, MainActivity.class);
                  //  startActivity(intent);
               //     setContentView(new GameView(this));
                    setContentView(new GameView(this));
                }break;

                //выход
                case R.id.button2: {
                    stopService(new Intent(this, MyService.class));
                    finish();
                }break;

                default:
                    break;
            }
        }
    }

