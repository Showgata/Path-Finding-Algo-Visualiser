package com.kanuma.linechart;

import java.util.HashMap;

public class NodeManager {

    private Node[][] nodeMatrix;
    private static HashMap<String,Node> nodeHashMap = new HashMap<>();

    public NodeManager(Node[][] nodeMatrix) {
        this.nodeMatrix = nodeMatrix;
        createHash();
    }

    private void createHash() {

        for(int i=0;i<nodeMatrix.length;i++) {
            for (int j = 0; j < nodeMatrix.length; j++) {
                nodeHashMap.put(i+","+j, nodeMatrix[i][j]);
            }
        }
    }

    public static Node getNode(String key){
        return nodeHashMap.get(key);
    }


}
