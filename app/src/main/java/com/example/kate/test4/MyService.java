package com.example.kate.test4;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {

    }
    private static final String TAG = "MyService";
    MediaPlayer player;


    @Override
    public void onCreate() {
      //  Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();

        player = MediaPlayer.create(this, R.raw.winter);
        player.setLooping(true); // зацикливаем
    }


    public void onDestroy() {
        //  Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        player.stop();
    }


    public void onStart(Intent intent, int startid) {
//         Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        player.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
