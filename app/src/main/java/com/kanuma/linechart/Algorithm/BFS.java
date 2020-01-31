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
    private boolean visited;
    private Node startNode;
    private Node goalNode;
    private static int i =0;
    private Node currentNode;

    private double tempG =0;
    LinkedList<Node> path;

    public BFS(Node[][] nodeMatrix, Node startNode, Node goalNode) {
        this.nodeMatrix = nodeMatrix;
        this.startNode = startNode;
        this.goalNode = goalNode;

        //Set the g(x) of source node to zero
        startNode.setgCost(0);

        //Set the f(x)=h(x) of source node to zero
        startNode.sethCost(calculateHeuristicFun(startNode,goalNode));

        currentNode = startNode;
        openSet.add(startNode);
    }


    public void run(final View v){

        final Handler h =new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(openSet.size()>0){

//                    if(currentNode == goalNode) {
//                        Log.d(TAG, "run: GOAL STATE FOUND !!!");
//                        h.removeCallbacksAndMessages(null);
//                        calculateShortestPath(v);
//                        return; // if found use cameFrom to reconstruct the solution path
//                    }


                    currentNode = openSet.poll();
                    if(currentNode!=null && currentNode.getNodeType() != STATE_NODE.ALREADY_VISITED) {
//                        Log.d(TAG, "run: currentNode"+currentNode.getIndex().toString());
//                        v.invalidate();

                        for (Node neighbourNode : currentNode.getNeighbouringNodes()) {

                            tempG = currentNode.getgCost() + 1;
                            if(neighbourNode != null && tempG < neighbourNode.getgCost()){

                                if(neighbourNode.getNodeType() == STATE_NODE.OBSTACLE_NODE) continue;
                                //use came_from to record

                                neighbourNode.setgCost(tempG);
                                //neighbourNode.sethCost(calculateHeuristicFun(neighbourNode,goalNode));
                                neighbourNode.setNodeType(STATE_NODE.EXPLORING);
                                v.invalidate();

                                if(neighbourNode == goalNode) {
                                    Log.d(TAG, "run: GOAL STATE FOUND !!!");
                                    Log.d(TAG, "Distance : "+goalNode.getfCost());
                                    h.removeCallbacksAndMessages(null);
                                    calculateShortestPath(v);
                                    return; // if found use cameFrom to reconstruct the solution path
                                }
                            }

                            if(!openSet.contains(neighbourNode)) {
                                openSet.add(neighbourNode);

                            }
                        }

                        currentNode.setNodeType(STATE_NODE.ALREADY_VISITED);
                        v.invalidate();
                    }

                    h.postDelayed(this,100);
                }

            }
        },100);

    }

    //animate this
    private void calculateShortestPath(final View v) {

        Node currentNode = goalNode;
        path = new LinkedList<>();

        while(currentNode != startNode){
            path.add(currentNode);
            for(Node neightbourNode : currentNode.getNeighbouringNodes()){
                if(neightbourNode.getgCost() == currentNode.getgCost()-1){
                    currentNode = neightbourNode;
                    break;
                }
            }
            path.add(startNode);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Node r =path.poll();
                if(r != null){
                    r.setNodeType(STATE_NODE.FINAL);
                    Log.d(TAG, "run: "+r.getgCost());
                    v.invalidate();
                }else{
                    return;
                }
                handler.postDelayed(this,100);
            }
        },100);

    }


}
