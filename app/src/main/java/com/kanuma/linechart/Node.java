package com.kanuma.linechart;

import android.graphics.RectF;
import android.util.Pair;

public class Node {
    private Pair<Integer,Integer> indexInGrid;
    private float gCost=0;
    private float hCost=0;
    private float fCost =0;
    private STATE_NODE nodeType;
    private boolean walkable;
    private Node parent;
    private RectF rectPos;

    public Node(int x,int y,boolean walkable,STATE_NODE nodeType) {
        this.nodeType = nodeType;
        this.walkable = walkable;
        setIndex(x, y);
    }

    public float getfCost() {
        return hCost+gCost;
    }

    public void setNodeType(STATE_NODE nodeType) {
        this.nodeType = nodeType;
    }

    public RectF getRectPos() {
        return rectPos;
    }

    public void setRectPos(RectF rectPos) {
        this.rectPos = rectPos;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public STATE_NODE getNodeType() {
        return nodeType;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public float getgCost() {
        return gCost;
    }

    public void setgCost(float gCost) {
        this.gCost = gCost;
    }

    public float gethCost() {
        return hCost;
    }

    public void sethCost(float hCost) {
        this.hCost = hCost;
    }

    public void setIndex(int x, int y){
        this.indexInGrid = new Pair<>(x,y);
    }

    public Pair<Integer, Integer> getIndex(){
        return indexInGrid;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Node{" +
                "indexInGrid=(" + indexInGrid.first +","+indexInGrid.second+")"+
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", nodeType=" + nodeType +
                ", walkable=" + walkable +
                '}';
    }
}
