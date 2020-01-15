package com.kanuma.linechart.Algorithm;

import com.kanuma.linechart.Node;

public class DjikstraAlgorithm {

    private Node[][] nodeMatrix;
    private int startX;
    private int startY;

    public DjikstraAlgorithm(Node[][] nodeMatrix,int startX,int startY) {
        this.nodeMatrix =nodeMatrix;
        this.startX =startX;
        this.startY=startY;
    }
}
