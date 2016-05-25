package com.example.kate.test4.Map;
//точка пути

final public class WayPoint {
    private int x, y, px, py, cost;

    public WayPoint(int x, int y, int px, int py, int cost) {

        setData(x, y, px, py, cost);
    }

    private void setData(int x, int y, int px, int py, int cost) {
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;
        this.cost = cost;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPX() {
        return px;
    }

    public int getPY() {
        return py;
    }

    public int getCost() {
        return cost;
    }


}