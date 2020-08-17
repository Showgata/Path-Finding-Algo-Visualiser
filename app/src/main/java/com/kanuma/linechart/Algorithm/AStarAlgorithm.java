package com.kanuma.linechart.Algorithm;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.kanuma.linechart.Node;
import com.kanuma.linechart.STATE_NODE;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarAlgorithm extends Algo{

    private static final String TAG = "AStarAlgorithm";

    private PriorityQueue<Node> openSet;
    private Map<Node,Node> cameFrom = new HashMap<>();

    private Node[][] nodeMatrix;
    private Node startNode;
    private Node goalNode;
    private static int i =0;
    private Node currentNode;

    private double tempG =0;

    public AStarAlgorithm(Node[][] nodeMatrix, Node startNode, Node goalNode) {
        this.nodeMatrix = nodeMatrix;
        this.startNode = startNode;
        this.goalNode = goalNode;

        this.openSet= new PriorityQueue<Node>();
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

                    currentNode = openSet.poll();
                    if(currentNode!=null && currentNode.getNodeType() != STATE_NODE.ALREADY_VISITED) {
//                        Log.d(TAG, "run: currentNode"+currentNode.getIndex().toString());
//                        currentNode.setNodeType(STATE_NODE.EXPLORING);
//                        v.invalidate();

                        for (Node neighbourNode : currentNode.getNeighbouringNodes()) {

                            tempG = currentNode.getgCost() + 1;
                            if(neighbourNode != null && tempG < neighbourNode.getgCost()){

                                if(neighbourNode.getNodeType() == STATE_NODE.OBSTACLE_NODE) continue;

                                //use came_from to record
                                if(tempG <neighbourNode.getgCost()) {
                                    cameFrom.put(neighbourNode, currentNode);
                                    neighbourNode.setgCost(tempG);
                                    neighbourNode.sethCost(calculateHeuristicFun(neighbourNode, goalNode));
                                    neighbourNode.setNodeType(STATE_NODE.EXPLORING);
                                    v.invalidate();

                                if(!openSet.contains(neighbourNode)) {
                                    openSet.add(neighbourNode);
                                }
                                }
                            }

                            if(currentNode == goalNode) {
                                Log.d(TAG, "run: GOAL STATE FOUND !!!");
                                reconstructPath(v);
                                return;} // if found use cameFrom to reconstruct the solution path


                        }

                        currentNode.setNodeType(STATE_NODE.ALREADY_VISITED);
                        v.invalidate();
                    }

                    h.postDelayed(this,200);
                }

            }
        },200);

    }


    void reconstructPath(final View v){

        final LinkedList<Node> totalPath = new LinkedList<>();
        totalPath.push(startNode);

        for(Map.Entry<Node, Node> current : cameFrom.entrySet()){
            totalPath.push(current.getValue());
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Node r =totalPath.poll();
                if(r != null){
                    r.setNodeType(STATE_NODE.FINAL);
                    v.invalidate();
                }else{
                    return;
                }
                handler.postDelayed(this,1000);
            }
        },1000);


    }

}
