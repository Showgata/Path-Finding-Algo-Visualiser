package com.kanuma.linechart.Algorithm;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.kanuma.linechart.Node;
import com.kanuma.linechart.STATE_NODE;

import java.util.LinkedList;
import java.util.Queue;

public class BFS extends Algo{

    private static final String TAG = "BFS";

    private Queue<Node> openSet= new LinkedList<>();
    private LinkedList<Node> cameFrom = new LinkedList<>();

    private Node[][] nodeMatrix;
    private Node startNode;
    private Node goalNode;
    private static int i =0;
    private Node currentNode;

    private double tempG =0;

    public BFS(Node[][] nodeMatrix, Node startNode, Node goalNode) {
        this.nodeMatrix = nodeMatrix;
        this.startNode = startNode;
        this.goalNode = goalNode;

        //Set the g(x) of source node to zero
        startNode.setgCost(0);

        //Set the f(x)=h(x) of source node to zero
        startNode.sethCost(calculateHeuristicFun(startNode,goalNode));

        openSet.add(startNode);
    }


    public void run(final View v){

        final Handler h =new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(openSet.size()>0){

                    if(currentNode == goalNode) return; // if found use cameFrom to reconstruct the solution path

                    currentNode = openSet.poll();
                    if(currentNode!=null && currentNode.getNodeType() != STATE_NODE.ALREADY_VISITED) {
                        Log.d(TAG, "run: currentNode"+currentNode.getIndex().toString());
                        currentNode.setNodeType(STATE_NODE.EXPLORING);
                        v.invalidate();

                        for (Node neighbourNode : currentNode.getNeighbouringNodes()) {

                            tempG = currentNode.getgCost() + 1;
                            if(neighbourNode != null && tempG < neighbourNode.getgCost()){
                                //use came_from to record
                                neighbourNode.setgCost(tempG);
                                neighbourNode.sethCost(calculateHeuristicFun(neighbourNode,goalNode));
                            }

                            if(!openSet.contains(neighbourNode)) {
                                openSet.add(neighbourNode);
                                currentNode.setNodeType(STATE_NODE.EXPLORING);
                            }
                        }

                        currentNode.setNodeType(STATE_NODE.ALREADY_VISITED);
                        v.invalidate();
                    }

                    h.postDelayed(this,200);
                }

            }
        },200);

    }


}
