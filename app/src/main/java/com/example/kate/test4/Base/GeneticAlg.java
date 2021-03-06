package com.example.kate.test4.Base;


import com.example.kate.test4.Grafic.ATB;
import com.example.kate.test4.Grafic.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlg {
    private List<Sprite> spritesPlayer = new ArrayList<>();
    private List<Sprite> spritesBot = new ArrayList<>();
    private List<ATB> showRoundPlayer = new ArrayList<>();
    //фитнесс-функция, изменение координат игрока/бота
    private double[] xForAlg;
    private double[] yForAlg;
    //число хромосом в поколении
    private int hromosomLength;
    //число поколений
    private int populationLength;
    //кол-во генов в хромосоме
    private int genomLength = 7;
    private double mutationPercent;
    private int persForDamageLength;
    private int[][] genomListParents;
    private int[][] genomListOffsprings;
    private int[] actual;
    private int[] fitnessFunctionResult;
    private int currentGeneration = 0;
    public static final int OCTET_LENGTH = 32;
    public static final int SHIFT_FOR_DIVISION;

    static {
        int shiftForDivision = 0;
        int tmp = OCTET_LENGTH;
        while (tmp > 1) {
            tmp >>= 1;
            shiftForDivision++;
        }
        SHIFT_FOR_DIVISION = shiftForDivision;
    }

    public GeneticAlg( List spritesPlayer, List spritesBot, List showRoundPlayer) {
        this.hromosomLength = 10;
        this.spritesBot=spritesBot;
        this.spritesPlayer=spritesPlayer;
        this.showRoundPlayer=showRoundPlayer;
        //кол-во врагов + защита+ожидание+ход в 4 стороны
        this.persForDamageLength = spritesBot.size()+spritesPlayer.size() + 2 + 4;
        this.mutationPercent = this.genomLength * (1.5 - Math.pow((1 - 10 * Math.pow((1 / 2), (this.genomLength - 1))), (1 / this.genomLength)));
        populationLength = 10;
    }

    public int[] run() {
        genomListParents = new int[hromosomLength][];
        genomListOffsprings = new int[hromosomLength][];
        fitnessFunctionResult = new int[hromosomLength];
        actual = new int[hromosomLength];
        for (int i = 0; i < hromosomLength; i++) {
            actual[i] = -1;
        }

        generateFirstGeneration();

        while (this.currentGeneration < this.populationLength) {
            selection();
            crossing();
            mutation();
            int[][] tmp = genomListParents;
            genomListParents = genomListOffsprings;
            genomListOffsprings = tmp;
            currentGeneration++;

        }

        int bestFitnessFunctionResult = -100000;
        int[] bestGenom = null;
        for (int[] genom : genomListParents) {
            int fitnessFunctionResult = fitnessFunction(genom);
            if (bestFitnessFunctionResult <= fitnessFunctionResult) {
                bestGenom = genom;
                bestFitnessFunctionResult = fitnessFunctionResult;
            }
        }

        return bestGenom;
    }

    private void generateFirstGeneration() {
        for (int i = 0; i < hromosomLength; i++) {
            genomListParents[i] = generateGenom();
            fitnessFunction(genomListParents[i]);
        }
    }


    private int[] generateGenom() {
        int[] result = new int[genomLength];
        Random random = new Random();
        for (int i = 0; i < genomLength; i++) {
            result[i] = random.nextInt(persForDamageLength - 1);
        }
        return result;
    }

    private void selection() {
        //метод турнира
        Random random = new Random();
        for (int i = 0; i < hromosomLength; i++) {
            int index1 = random.nextInt(hromosomLength - 1);
            int index2 = random.nextInt(hromosomLength - 1);
            int fr1 = getFitnessFunctionResult(index1);
            int fr2 = getFitnessFunctionResult(index2);
            genomListOffsprings[i] = fr1 > fr2 ? genomListParents[index1].clone() : genomListParents[index2].clone();
        }


    }

    private int getFitnessFunctionResult(int genomNumber) {
        if (actual[genomNumber] != currentGeneration) {
            fitnessFunctionResult[genomNumber] = fitnessFunction(genomListParents[genomNumber]);
            actual[genomNumber] = currentGeneration;
        }
        return fitnessFunctionResult[genomNumber];
    }

    private void crossing() {

        for (int i = 0; i < hromosomLength / 2; i++) {
            int index1 = i << 1;
            int index2 = index1 | 1;
            cross(genomListOffsprings[index1], genomListOffsprings[index2]);
        }


    }

    private void cross(int[] genom1, int[] genom2) {
        //поэлементарное скрещивание
        Random random = new Random();
        int mask = random.nextInt(genomLength - 1);
        for (int outerOffset = 0; outerOffset < genomLength; outerOffset++) {
            if (outerOffset < mask) {
                genom1[outerOffset] = genom1[outerOffset];
                genom2[outerOffset] = genom1[outerOffset];
            } else {
                genom1[outerOffset] = genom2[outerOffset];
                genom2[outerOffset] = genom2[outerOffset];
            }
        }

    }

    private void mutation() {
        Random random = new Random();
        for (int[] genom : genomListOffsprings) {
            if (random.nextDouble() <= mutationPercent) {
                mutate(genom);
            }
        }
    }


    private void mutate(int[] genom) {
        Random random = new Random();
        int index = random.nextInt(genomLength - 1);
        genom[index] = random.nextInt(persForDamageLength - 1);
    }

    private int fitnessFunction(int[] genom) {
        int damage = 0;
        int[] remarks;
        remarks = new int[genom.length];
        setXY();
        for (int i = 0; i < genom.length; i++) {
            Random random = new Random();
            int flag = random.nextInt(persForDamageLength - 1);
            //если до этого было нажато ожидание и персонаж играет 2 раз
            if (remarks[i] == 1)
                if (flag < persForDamageLength - 2 - 4) {
                    damage = damage + getDamage(i - 3, flag, remarks);
                    continue;
                }
            if (genom[i] < persForDamageLength - 2 - 4) {

                damage = damage + getDamage(i, genom[i], remarks);
            } else {
                //ожидание
                if (genom[i] == persForDamageLength - 2 - 4) {
                    if (i + 3 < genomLength)
                        remarks[i + 3] = 1;
                    continue;
                }
                if (genom[i] == persForDamageLength - 1 - 4) {
                    //защита
                    remarks[i] = 30;
                } else {
                    changeXY(i, genom[i]);
                }

            }

        }

        return damage;
    }
    //фитнесс-функция
    protected int getDamage(int i, int whoDef, int[] remarks) {

        int damage, whoGoAlg, whoPlayAlg;
        boolean defance = false;
        int flag = showRoundPlayer.get(i).getSizeInList();
        if (flag - spritesBot.size() < 0)
            whoPlayAlg = 1;
        else whoPlayAlg = 0;
        whoGoAlg = flag - ((flag - 6 < 0) ? 0 : 6);
        assert (whoGoAlg<6);
        if (whoPlayAlg == 0 && whoDef < spritesPlayer.size() || whoPlayAlg == 1 && whoDef >= spritesPlayer.size() && whoDef < spritesPlayer.size() + spritesBot.size())
            return 0;
        for (int j = 0; j < i; j++) {
            if (remarks[j] == 30) {
                flag = showRoundPlayer.get(j).getSizeInList();
                if (flag - 6 < 0 && whoPlayAlg == 0)
                    //бот защищается
                    if (whoDef == flag - ((flag - 6 < 0) ? 0 : 6))
                        defance = true;
                if (flag - 6 > 0 && whoPlayAlg == 1) {
                    //игрок защищается
                    if (whoDef == flag - ((flag - 6 < 0) ? 0 : 6))
                        defance = true;
                }
            }


        }
        int shot = ((whoPlayAlg == 0 && whoGoAlg == 3 || whoPlayAlg == 1 && (whoGoAlg == 0 || whoGoAlg == 3)) ? 1 : 0);
        if (shot == 1) {
            //тк стрелок - можем стерять в любого
            if (whoPlayAlg == 0) {

                damage = spritesPlayer.get(whoGoAlg).getAttack((int) xForAlg[whoDef], (int) yForAlg[whoDef],
                        (int) (spritesBot.get(whoDef - spritesPlayer.size()).getDefence() * ((defance) ? 1.3 : 1)), shot);
                assert (damage>0);
                return -damage;

            } else {

                damage = spritesBot.get(whoGoAlg).getAttack((int) xForAlg[whoDef], (int) yForAlg[whoDef],
                        (int) (spritesPlayer.get(whoDef).getDefence() * ((defance) ? 1.3 : 1)), shot);
                assert (damage>0);
                return damage;
            }
        } else
        //проверка на возможность нападения
        {
            if (whoPlayAlg == 0) {
                //если возможен удар по клеткам
                if (xForAlg[whoDef] > xForAlg[whoGoAlg] - spritesPlayer.get(whoGoAlg).getStep() && xForAlg[whoDef] < xForAlg[whoGoAlg] + spritesPlayer.get(whoGoAlg).getStep() &&
                        yForAlg[whoDef] > yForAlg[whoGoAlg] - spritesPlayer.get(whoGoAlg).getStep() && yForAlg[whoDef] < yForAlg[whoGoAlg] + spritesPlayer.get(whoGoAlg).getStep()) {
                    damage = spritesPlayer.get(whoGoAlg).getAttack((int) spritesBot.get(whoDef - spritesPlayer.size()).getX(), (int) spritesBot.get(whoDef - spritesPlayer.size()).getY(),
                            (int) (spritesBot.get(whoDef - spritesPlayer.size()).getDefence() * ((defance) ? 1.3 : 1)), shot);
                    assert (damage>0);
                    return -damage;
                } else return 0;
            } else {
                //если возможен удар по клеткам
                if (xForAlg[whoDef] > xForAlg[whoGoAlg + spritesPlayer.size() - 1] - spritesBot.get(whoGoAlg).getStep() && xForAlg[whoDef] < xForAlg[whoGoAlg + spritesPlayer.size() - 1] + spritesBot.get(whoGoAlg).getStep() &&
                        yForAlg[whoDef] > yForAlg[whoGoAlg + spritesPlayer.size() - 1] - spritesBot.get(whoGoAlg).getStep() && yForAlg[whoDef] < yForAlg[whoGoAlg + spritesPlayer.size() - 1] + spritesBot.get(whoGoAlg).getStep()) {
                    damage = spritesBot.get(whoGoAlg).getAttack((int) spritesPlayer.get(whoDef).getX(), (int) spritesPlayer.get(whoDef).getY(),
                            (int) (spritesPlayer.get(whoDef).getDefence() * ((defance) ? 1.3 : 1)), shot);
                    assert (damage>0);
                    return damage;
                } else return 0;

            }


        }
    }

    //фитнесс-функция
    protected void setXY() {
        xForAlg = new double[spritesBot.size() + spritesPlayer.size()];
        yForAlg = new double[spritesBot.size() + spritesPlayer.size()];
        int i = 0;
        for (Sprite sprite : spritesPlayer) {
            if (!sprite.getDead()) {
                xForAlg[i] = sprite.getX();
                yForAlg[i] = sprite.getY();
            }
            i++;
        }
        for (Sprite sprite : spritesBot) {
            if (!sprite.getDead()) {
                xForAlg[i] = sprite.getX();
                yForAlg[i] = sprite.getY();
            }
            i++;
        }

    }

    //фитнесс-функция
    protected void changeXY(int i, int flag) {
        int whoGoAlg, whoPlayAlg;
        int fl = showRoundPlayer.get(i).getSizeInList();
        assert (fl<spritesBot.size()+spritesPlayer.size());
        if (fl - 6 < 0)
            whoPlayAlg = 1;
        else whoPlayAlg = 0;
        whoGoAlg = fl - ((fl - 6 < 0) ? 0 : 6);
        switch (flag) {
            case 14: {
                //право вверх
                makePath(whoPlayAlg,whoGoAlg,1,1);
                break;
            }
            case 15: {
                //лево вверх
                makePath(whoPlayAlg,whoGoAlg,-1,1);
                break;
            }
            case 16: {
                //право вниз
                makePath(whoPlayAlg,whoGoAlg,1,-1);
                break;
            }
            case 17: {
                //влево вниз
                makePath(whoPlayAlg,whoGoAlg,-1,-1);
                break;
            }

        }
    }
    private void makePath(int whoPlayAlg, int whoGoAlg, int x, int y){
        int j=0;
        if (whoPlayAlg == 0) {
            while (j < spritesPlayer.get(whoGoAlg).getStep() ) {
                if (tileIsPossible((int) xForAlg[whoGoAlg] +x, (int) yForAlg[whoGoAlg]))
                    xForAlg[whoGoAlg] += x;
                if (tileIsPossible((int) xForAlg[whoGoAlg], (int) yForAlg[whoGoAlg] +y))
                    yForAlg[whoGoAlg] += y;
                j++;
            }

        } else {
            while (j < spritesBot.get(whoGoAlg).getStep() ) {
                if (tileIsPossible((int) xForAlg[whoGoAlg + spritesPlayer.size()] +x, (int) yForAlg[whoGoAlg + spritesPlayer.size()]))
                    xForAlg[whoGoAlg + spritesPlayer.size()] += x;
                if (tileIsPossible((int) xForAlg[whoGoAlg + spritesPlayer.size()], (int) yForAlg[whoGoAlg + spritesPlayer.size()]+y))
                    yForAlg[whoGoAlg + spritesPlayer.size()] += y;
                j++;
            }
        }


    }
    //возможно ли сделать шаг
    public boolean possibleMove(int x, int y) {
        for (Sprite sprite : spritesBot) {
            if (sprite.getX() == x && sprite.getY() == y && !sprite.getDead())
                return false;
        }
        for (Sprite sprite : spritesPlayer) {
            if (sprite.getX() == x && sprite.getY() == y && !sprite.getDead())
                return false;
        }

        return true;
    }

    public boolean tileIsPossible(int x, int y) {

        return ( x < 10 && x > 0 && y < 16 && possibleMove(x, y));
    }
}