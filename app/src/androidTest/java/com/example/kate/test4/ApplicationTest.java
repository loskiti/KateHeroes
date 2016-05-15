package com.example.kate.test4;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {
    private List<SpriteTest> spritesPlayer = new ArrayList<SpriteTest>();
    private List<SpriteTest> spritesBot = new ArrayList<SpriteTest>();
    private List<ATBTest> showRoundPlayer = new ArrayList<ATBTest>();
    private List<ATBTest> roundPlayer = new ArrayList<ATBTest>();
    private double[] xForAlg;
    private double[] yForAlg;
    public void test() throws Exception {
        createSprites();
        doOrder();
        int j = 0;
        for(int k=0;k<7;k++) {
            if (showRoundPlayer.get(k).getSizeInList()<spritesBot.size()) {


            for (int i = 0; i < 1001; i++) {
                GeneticAlgTest ge = new GeneticAlgTest(spritesPlayer.size() - 1 + spritesBot.size(), spritesPlayer, spritesBot, showRoundPlayer);
                int[] better = ge.run();
                if (better[k] ==spritesPlayer.size()+1 )
                    j++;
                Log.d("TEST", Integer.toString(better[0]));
            }
            break;
        }

    }
        Log.d("TEST5555", Integer.toString(j));
    }


    private void createSprites() {


       /* spritesBot.add(createSprite(R.drawable.bad6, 9, 2, 5, 3, 1, 2, 6, 3, 12, 20, 10, true, true));
        spritesBot.add(createSprite(R.drawable.bad9, 9, 4, 3, 5, 1, 2, 20, 3, 10, 14, 11, true, false));
        spritesBot.add(createSprite(R.drawable.bad8, 9, 6, 6, 6, 5, 7, 24, 2, 7, 9, 7, true, false));
        spritesBot.add(createSprite(R.drawable.bad10, 9, 8, 12, 9, 7, 7, 29, 2, 10, 5, 5, true, true));*/
        spritesBot.add(createSprite(R.drawable.bad11, 9, 10, 14, 14, 14, 19, 45, 3, 12, 3, 3, true, false));
        spritesBot.add(createSprite(R.drawable.bad12, 9, 12, 27, 20, 25, 35, 135, 3, 8, 2, 2, true, false));


        spritesPlayer.add(createSprite(R.drawable.bad1, 1, 2, 3, 3, 1, 4, 6, 2, 13, 16, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad2, 1, 4, 3, 4, 1, 4, 13, 2, 8, 15, 20, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad3, 1, 6, 4, 2, 4, 6, 15, 3, 13, 8, 12, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad4, 1, 8, 6, 6, 6, 13, 32, 1, 9, 5, 6, false, true));
        spritesPlayer.add(createSprite(R.drawable.bad5, 1, 10, 18, 17, 10, 17, 66, 2, 15, 3, 2, false, false));
        spritesPlayer.add(createSprite(R.drawable.bad7, 1, 12, 27, 23, 13, 31, 140, 2, 9, 2, 1, false, false));


    }
    private SpriteTest createSprite(int resource, int x, int y, int damage,
                                int defence, int minAttack, int maxAttack, int health, int step, int initiative, int morale, int number, boolean isBot, boolean shot) {

        return new SpriteTest(  x, y, damage, defence, minAttack, maxAttack, health, step, initiative, morale, number, isBot, shot, false);
    }
    private void doOrder() {

        int flag = 0;
       /* for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int ran = random.nextInt(10);
            roundPlayer.add(createATB(ran, ran, spritesBot.get(i).getInit(), true, i));
            ran = random.nextInt(10);
            roundPlayer.add(createATB(ran, ran, spritesPlayer.get(i).getInit(), true, i + 6));
        }*/
        for(int i=0;i<spritesBot.size();i++){
            Random random = new Random();
            int ran = random.nextInt(10);
            roundPlayer.add(createATB(ran, ran, spritesBot.get(i).getInit(), true, i));
        }
        for(int i=0;i<spritesPlayer.size();i++){
            Random random = new Random();
            int ran = random.nextInt(10);
            roundPlayer.add(createATB(ran, ran, spritesPlayer.get(i).getInit(), true, i+spritesBot.size()));
        }
        int z = 0;
        while (flag < 7) {
            for (int i = 0; i < roundPlayer.size(); i++) {
                if (roundPlayer.get(i).getIfFirstRound())
                    roundPlayer.get(i).firstStep();
                else roundPlayer.get(i).step();
                if (roundPlayer.get(i).getCurABT() >= 100) {
                    flag = flag + 1;
                    roundPlayer.get(i).changeCurATB(-100);
                    roundPlayer.get(i).changeIfFirstRound();
                    showRoundPlayer.add(roundPlayer.get(i));

                }
            }
        }
    }
        private ATBTest createATB(int curATB, int startATB, int ini, boolean ifFirstRound, int sizeInList) {

            return new ATBTest( curATB, startATB, ini, ifFirstRound, sizeInList);
        }
}

