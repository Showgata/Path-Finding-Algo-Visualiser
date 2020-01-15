package com.kanuma.linechart.Algorithm;

import android.util.Pair;

import com.kanuma.linechart.Node;

import java.util.LinkedList;

public class AStarAlgorithm extends Algo{

    private LinkedList<Node> openSet= new LinkedList<>();
    private Node[][] nodeMatrix;
    private Node startIndex;
    private Node goalIndex;

    public AStarAlgorithm(Node[][] nodeMatrix, Node startIndex, Node goalIndex) {
        this.nodeMatrix = nodeMatrix;
        this.startIndex = startIndex;
        this.goalIndex = goalIndex;
        openSet.push(startIndex);
    }


    void run(){

        while (openSet.size()>0){

        }
    }
}
