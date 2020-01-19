package com.kanuma.linechart.Algorithm;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.kanuma.linechart.Node;
import com.kanuma.linechart.STATE_NODE;

import java.util.LinkedList;

public class AStarAlgorithm extends Algo{

    private static final String TAG = "AStarAlgorithm";

    private LinkedList<Node> openSet= new LinkedList<>();
    private Node[][] nodeMatrix;
    private Node startIndex;
    private Node goalIndex;
    private static int i =0;
    private Node currentNode;

    public AStarAlgorithm(Node[][] nodeMatrix, Node startIndex, Node goalIndex) {
        this.nodeMatrix = nodeMatrix;
        this.startIndex = startIndex;
        this.goalIndex = goalIndex;
        startIndex.setgCost(0);
        openSet.push(startIndex);
    }


    public void run(final View v){

        final Handler h =new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(openSet.size()>0 && currentNode != goalIndex){

                    currentNode = openSet.poll();
                    if(currentNode!=null && currentNode.getNodeType() != STATE_NODE.ALREADY_VISITED) {
                        Log.d(TAG, "run: currentNode"+currentNode.getIndex().toString());
                        currentNode.setNodeType(STATE_NODE.EXPLORING);
                        v.invalidate();
                        for (Node neighbourNode : currentNode.getNeighbouringNodes()) {
                            neighbourNode.setgCost(currentNode.getgCost()+1);
                            openSet.push(neighbourNode);
                        }
                        currentNode.setNodeType(STATE_NODE.EXPLORING);
                        v.invalidate();
                    }

                    h.postDelayed(this,200);
                }

            }
        },200);

    }


}
