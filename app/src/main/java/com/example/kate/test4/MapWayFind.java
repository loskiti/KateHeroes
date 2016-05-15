package com.example.kate.test4;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MapWayFind {
    private MapWay mapway;
    private IMapCheckPoint checkPoint;
    private boolean isFinish;
    private List<WayPoint> nextPoint;
    private List<WayPoint> backPoint;

    public MapWay getWay() {
        return mapway;
    }

    public void setcheckPoint(IMapCheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    public boolean findWay(int startx, int starty, int endx, int endy) {
        isFinish = false;
        mapway = new MapWay();
        nextPoint = new ArrayList<WayPoint>();
        backPoint = new ArrayList<WayPoint>();

        WayPoint p = new WayPoint(startx, starty, -1, -1, Math.abs(startx - endx) + Math.abs(starty - endy), true);
        nextPoint.add(p);
        backPoint.add(p);
        WayPoint node;
        while (nextPoint.size() > 0) {

            node = nextPoint.get(0);
            nextPoint.remove(0);
            if (node.getX() == endx && node.getY() == endy) {
                makeWay(node);
                isFinish = true;

                break;
            } else {
                node.setVisited(true);
                addNode(node, node.getX() + 1, node.getY(), endx, endy);
                addNode(node, node.getX() - 1, node.getY(), endx, endy);
                addNode(node, node.getX(), node.getY() + 1, endx, endy);
                addNode(node, node.getX(), node.getY() - 1, endx, endy);

            }
        }

        backPoint = null;
        nextPoint = null;

        return isFinish;
    }

    private void makeWay(WayPoint node) {
        mapway.clear();
        while (node.getPX() != -1) {
            mapway.addPoint(new Point(node.getX(), node.getY()));
            for (WayPoint p : backPoint) {
                if (p.getX() == node.getPX() && p.getY() == node.getPY()) {
                    node = p;
                    break;
                }
            }
        }
    }

    private void addNode(WayPoint node, int x, int y, int endx, int endy) {
        if (checkPoint.check(x, y)) {
            int cost = Math.abs(x - endx) + Math.abs(y - endy);
            WayPoint px = new WayPoint(x, y, node.getX(), node.getY(), cost, false);
            WayPoint old = null;
            for (WayPoint p : backPoint) {
                if (p.getX() == px.getX() && p.getY() == px.getY()) {
                    old = p;
                    break;
                }
            }

            if (old == null || old.getCost() > cost) {
                backPoint.add(px);
                int i = 0;
                for (i = 0; i < nextPoint.size(); i++) {
                    if (cost < nextPoint.get(i).getCost()) {
                        nextPoint.add(i, px);
                        break;
                    }
                }
                if (i >= nextPoint.size()) {
                    nextPoint.add(px);
                }
            }
        }
    }
}