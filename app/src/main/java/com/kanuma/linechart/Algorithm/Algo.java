package com.kanuma.linechart.Algorithm;


import android.util.Pair;

import com.kanuma.linechart.Node;

public abstract class Algo {

    enum Name {BFS,DFS,A_STAR};

    public Algo execute(Name algoName, Node[][] nodeMatrix, Node startIndex, Node goalIndex){

        if(algoName == Name.BFS){
            return new BFS(nodeMatrix,startIndex,goalIndex);
        }else if(algoName == Name.A_STAR){
            return new AStarAlgorithm(nodeMatrix,startIndex,goalIndex);
        }

        return null;
    }


}
