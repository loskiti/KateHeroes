package com.example.kate.test4;


import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MapWay {
    public List<Point> way;
    private Point p;
    /**
     * current point
     */
    private int index = 0;

    public MapWay() {
        way = new ArrayList<Point>();
    }

    public void clear() {
        way.clear();
    }

    public void addPoint(Point p) {
        way.add(p);
    }

    /**
     * start point
     */
    public void startPoint(int x, int y) {
        p = new Point(x, y);
        index = way.size() - 1;
    }

    /**
     * is point or no
     */

    public boolean isNextPoint() {
        if (index > -1)
            return true;
        return false;
    }

    /**
     * next point(if this is passed)
     */

    public Point nextPoint(int step) {
        int x = way.get(index).x*120 , y = way.get(index).y*72 ;

        if (p.x != x) {
            p.x += step * ((x < p.x) ? -1 : 1);
        }
        if (p.y != y) {
            p.y += step * ((y < p.y) ? -1 : 1);
        }

        if (p.x == x && p.y == y) {
            index--;

        }

        return p;
    }

    public Point getP() {
        return p;
    }
}