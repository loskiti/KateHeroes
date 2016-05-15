package com.example.kate.test4;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

//музыка
public class MyService extends Service {
    public MyService() {

    }

    private static final String TAG = "MyService";
    MediaPlayer player;


    @Override
    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.winter);
        player.setLooping(true); // зацикливаем
    }


    public void onDestroy() {
        player.stop();
    }


    public void onStart(Intent intent, int startid) {
        player.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
