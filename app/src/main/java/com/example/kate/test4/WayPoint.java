package com.example.kate.test4;
//точка пути

final public class WayPoint {
    private int x, y, px, py, cost;
    private boolean visited;

    public WayPoint(int x, int y, int px, int py, int cost, boolean visited) {

        setData(x, y, px, py, cost, visited);
    }

    final private void setData(int x, int y, int px, int py, int cost, boolean visited) {
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;
        this.cost = cost;
        this.visited = visited;

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

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}