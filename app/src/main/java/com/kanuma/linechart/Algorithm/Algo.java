package com.kanuma.linechart.Algorithm;


import android.util.Pair;
import android.view.View;

import com.kanuma.linechart.Node;

public abstract class Algo {
    public enum Name {BFS,DFS,A_STAR};

    public static void execute(Name algoName, Node[][] nodeMatrix, Node startIndex, Node goalIndex, View view){

        if(algoName == Name.BFS){
            (new BFS(nodeMatrix,startIndex,goalIndex)).run(view);
        }else if(algoName == Name.A_STAR){
            (new AStarAlgorithm(nodeMatrix,startIndex,goalIndex)).run(view);
        }
        else if(algoName == Name.DFS){
            (new DFS(nodeMatrix,startIndex,goalIndex)).run(view);
        }

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
