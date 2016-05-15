package com.example.kate.test4;


import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map {
    private Context mContext;
    private int map[][];

    public Map(Context context) {

        this.mContext = context;

        int n = 23, m = 12;

        map = new int[n][m];


        AssetManager am = mContext.getAssets();

        try {
            InputStream is = am.open("in.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int j = 0;
            while ((line = reader.readLine()) != null) {

                for (int i = 0; i < m; i++) {
                    map[j][i] = Character.getNumericValue(line.charAt(i));

                }
                j++;
            }

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

    public int getTileName(int id) {
        switch (id) {
            //трава
            case 1:
                return (R.drawable.pis);
            default:
                return (R.drawable.piskel);
        }
    }

    public int getTile(int x, int y) {
        if (isTile(x, y))
            return map[y][x];
        return 0;

    }


}
