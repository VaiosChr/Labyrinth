package com.company;

import java.util.ArrayList;

/**
 * the class for the nodes of the tree of the available positions
 */
public class Node
{
    Node parent;
    ArrayList<Node> children = new ArrayList<>();
    int nodeDepth;
    int nodeMove;
    int nodeEvaluation;

    //constructors
    public Node(){}

    public Node(Node parent, int nodeDepth, int nodeMove)
    {
        this.parent = parent;
        this.nodeDepth = nodeDepth;
        this.nodeMove = nodeMove;
    }

    //setter
    public void setNodeEvaluation(int set)
    {
        nodeEvaluation = set;
    }

    /**
     * adds a node as a child of this node
     * @param child the node to add as a child of this node
     */
    public void addChild(Node child)
    {
        children.add(child);
    }

    //getters
    public ArrayList<Node> getChildren()
    {
        return children;
    }

    public int getNodeMove()
    {
        return nodeMove;
    }

    public int getNodeEvaluation()
    {
        return nodeEvaluation;
    }
}
