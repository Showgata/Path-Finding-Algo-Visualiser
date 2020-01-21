package com.kanuma.linechart.Algorithm;


import android.util.Pair;

import com.kanuma.linechart.Node;

public abstract class Algo {
    public enum Name {BFS,DFS,A_STAR};

    public static Algo execute(Name algoName, Node[][] nodeMatrix, Node startIndex, Node goalIndex){

        if(algoName == Name.BFS){
            return new BFS(nodeMatrix,startIndex,goalIndex);
        }else if(algoName == Name.A_STAR){
            return new AStarAlgorithm(nodeMatrix,startIndex,goalIndex);
        }
        return null;
    }

    private double calculateEuclideanDistance(double x1,double y1,double x2,double y2){
        double a = Math.pow(x1 - x2,2);
        double b = Math.pow(y1 - y2,2);
        return Math.sqrt(a+b);
    }

    public double calculateHeuristicFun(Node n1,Node n2){

        double x1 = n1.getRectPos().centerX();
        double y1 = n1.getRectPos().centerY();

        double x2 = n2.getRectPos().centerX();
        double y2 = n2.getRectPos().centerY();

        return calculateEuclideanDistance(x1,y1,x2,y2);
    }




}
