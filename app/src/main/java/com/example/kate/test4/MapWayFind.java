package com.example.kate.test4;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MapWayFind {
    private MapWay mapway;
    /**
     * move is possible or no
     */
    private IMapCheckPoint checkPoint;
    /**
     * is it finish or no
     */
    private boolean isFinish;
    /**
     *potentially possible point of the way
     */
    private List<WayPoint> nextPoint;
    /**
     *  now found points of the way
     */
    private List<WayPoint> backPoint;

    public MapWay getWay() {
        return mapway;
    }

    public void setcheckPoint(IMapCheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    /**
     * way find
     */
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
            // first potentially possible point

            node = nextPoint.get(0);
            nextPoint.remove(0);

            // is it end or no

            if (node.getX() == endx && node.getY() == endy) {

                // make way
                makeWay(node);
                isFinish = true;

                break;
            } else {
                // the point is scan
                node.setVisited(true);
                // potentially possible point
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

    /**
     * make way
     */

    private void makeWay(WayPoint node) {
        mapway.clear();
        while (node.getPX() != -1) {
            mapway.addPoint(new Point(node.getX(), node.getY()));
            // previous point
            for (WayPoint p : backPoint) {
                if (p.getX() == node.getPX() && p.getY() == node.getPY()) {
                    node = p;
                    break;
                }
            }
        }
    }

    /**
     * add potentially possible point
     */

    private void addNode(WayPoint node, int x, int y, int endx, int endy) {
        if (checkPoint.check(x, y)) {
            int cost = Math.abs(x - endx) + Math.abs(y - endy);
            WayPoint px = new WayPoint(x, y, node.getX(), node.getY(), cost, false);
            WayPoint old = null;

            // is point unique or no (p-start)
            for (WayPoint p : backPoint) {
                if (p.getX() == px.getX() && p.getY() == px.getY()) {
                    old = p;
                    break;
                }
            }
            // point is unique and  price of new point is under that old point

            if (old == null || old.getCost() > cost) {
                backPoint.add(px);
                int i = 0;
                for (i = 0; i < nextPoint.size(); i++) {
                    // point with under price is priority
                    if (cost < nextPoint.get(i).getCost()) {
                        nextPoint.add(i, px);
                        break;
                    }
                }
                // if point isn't add, add it in the end
                if (i >= nextPoint.size()) {
                    nextPoint.add(px);
                }
            }
        }
    }
}