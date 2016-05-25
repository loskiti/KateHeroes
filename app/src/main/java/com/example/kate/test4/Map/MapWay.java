package com.example.kate.test4.Map;


import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MapWay {
    public List<Point> way;

    public MapWay() {
        way = new ArrayList<>();
    }

    public void clear() {
        way.clear();
    }

    public void addPoint(Point p) {
        way.add(p);
    }


}