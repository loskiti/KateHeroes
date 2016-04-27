package com.example.kate.test4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public  class Map {
    private Context mContext;
    private int map [][];

    public Map(Context context)  {

        this.mContext = context;

       int n = 19, m = 12;

        map = new int[n][m];


        AssetManager am = mContext.getAssets();

        try {
            InputStream is = am.open("in.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int j=0;
            while ((line = reader.readLine()) != null) {

                for (int i = 0; i < m; i++) {
                    map[j][i]= Character.getNumericValue(line.charAt(i));

                }
                j++;
            }
               // mLines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int getMapWidth() {
        return map[0].length;
    }
    public int getMapHeight() {
        return map.length;
    }
    public int getMapTile(int x, int y) {
        if (isTile(x, y))
            return map[y][x];
        return 0;

    }
    private boolean isTile(int x, int y) {
        if ((x >= 0) && (y >= 0) && (y < getMapHeight()) && (x < getMapWidth()))
            return true;
        return false;
    }
    public int getTileName(int id){
        switch(id){
            //трава
            case 0: return (R.drawable.p0);
            case 1:return (R.drawable.pis);
            case 2: return (R.drawable.p0);
            //вода
            case 3:return (R.drawable.picsel9);
            case 4:return (R.drawable.picsel10);
            case 5:return (R.drawable.pic);
            case 6:return (R.drawable.picsel4);
            case 7:return (R.drawable.picsel5);
            case 8:return (R.drawable.picsel3);
            case 9:return (R.drawable.picsel11);
            case 10:return (R.drawable.picsel12);
            default:return(R.drawable.piskel);
        }
    }
    public int getTile(int x, int y) {
        if (isTile(x, y))
            return map[y][x];
        return 0;

    }


}
