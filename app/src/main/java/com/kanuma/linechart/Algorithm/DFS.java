package com.kanuma.linechart.Algorithm;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.kanuma.linechart.Node;
import com.kanuma.linechart.STATE_NODE;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFS extends Algo{

    private static final String TAG = "DFS";

    private Stack<Node> openSet= new Stack<>();
    private LinkedList<Node> cameFrom = new LinkedList<>();

    private Node[][] nodeMatrix;
    private boolean visited;
    private Node startNode;
    private Node goalNode;
    private static int i =0;
    private Node currentNode;

    private double tempG =0;
    LinkedList<Node> path;

    public DFS(Node[][] nodeMatrix, Node startNode, Node goalNode) {
        this.nodeMatrix = nodeMatrix;
        this.startNode = startNode;
        this.goalNode = goalNode;

        //Set the g(x) of source node to zero
        startNode.setgCost(0);

        //Set the f(x)=h(x) of source node to zero
        //startNode.sethCost(calculateHeuristicFun(startNode,goalNode));

        currentNode = startNode;
        openSet.push(startNode);
    }


    public void run(final View v){

        final Handler h =new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(openSet.size()>0){



                    currentNode = openSet.pop();
                    if(currentNode!=null && currentNode.getNodeType() != STATE_NODE.ALREADY_VISITED) {
//                        Log.d(TAG, "run: currentNode"+currentNode.getIndex().toString());
//                        v.invalidate();

                        for (Node neighbourNode : currentNode.getNeighbouringNodes()) {

                            tempG = currentNode.getgCost() + 1;
                            if(neighbourNode != null && tempG < neighbourNode.getgCost()){

                                if(neighbourNode.getNodeType() == STATE_NODE.OBSTACLE_NODE
                                || neighbourNode.getNodeType() == STATE_NODE.ALREADY_VISITED) continue;
                                //use came_from to record

                                neighbourNode.setgCost(tempG);
                                //neighbourNode.sethCost(calculateHeuristicFun(neighbourNode,goalNode));
                                neighbourNode.setNodeType(STATE_NODE.EXPLORING);
                                v.invalidate();

                                if( neighbourNode == goalNode ) {
                                    h.removeCallbacksAndMessages(null);
                                    calculateShortestPath(v);
                                    return; // if found use cameFrom to reconstruct the solution path
                                }

                            }

                            if(openSet.contains(neighbourNode)) {
                                openSet.remove(neighbourNode);

                            }
                            openSet.push(neighbourNode);
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
                if(neightbourNode == null) continue;
                if(neightbourNode.getgCost() == currentNode.getgCost()-1){
                    currentNode = neightbourNode;
                    Log.d(TAG, "calculateShortestPath: gCost="+currentNode.getgCost());
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
                    v.invalidate();
                }else{
                    return;
                }
                handler.postDelayed(this,100);
            }
        },100);

    }


}
