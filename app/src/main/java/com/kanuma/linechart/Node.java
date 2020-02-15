package com.kanuma.linechart;

import android.graphics.RectF;
import android.util.Pair;

import java.util.ArrayList;

public class Node implements Comparable{
    private Pair<Integer,Integer> indexInGrid;
    private double gCost=0;
    private double hCost=0;
    private double fCost =0;
    private STATE_NODE nodeType;
    private boolean walkable;
    private Node parent;
    private RectF rectPos;
    private ArrayList<Node> neighbouringNodes;

    public Node(int x,int y,boolean walkable,STATE_NODE nodeType) {
        this.nodeType = nodeType;
        this.walkable = walkable;
        this.neighbouringNodes = new ArrayList<>();
        this.gCost=99999;
        setIndex(x, y);
    }

    public double getfCost() {
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

    public double getgCost() {
        return gCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void sethCost(double hCost) {
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

    public void setNeighbouringNodes(NodeManager nodeManager) {

        String top,bottom,left,right;

        int x = indexInGrid.first;
        int y = indexInGrid.second;

        top = x+","+(y-1);
        bottom = x+","+(y+1);
        right = (x+1)+","+y;
        left = (x-1)+","+y;



        neighbouringNodes.add(nodeManager.getNode(bottom));
        neighbouringNodes.add(nodeManager.getNode(right));
        neighbouringNodes.add(nodeManager.getNode(top));
        neighbouringNodes.add(nodeManager.getNode(left));


    }

    public ArrayList<Node> getNeighbouringNodes(){
        if(neighbouringNodes.size() >0 ) return neighbouringNodes;
        return null;
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

    @Override
    public int compareTo(Object o) {

        if(this.getfCost() > ((Node)o).getfCost())
            return 1;
        else if(this.getfCost() < ((Node)o).getfCost())
            return -1;
        return 0;
    }
}
