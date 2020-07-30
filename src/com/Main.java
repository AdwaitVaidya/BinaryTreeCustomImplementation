package com;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Node<T>{
    private T value;
    private Node parentNode;
    private Node leftChild;
    private Node rightChild;
    private boolean alreadyTransversed = false;
    private int level;

    Node(T value){
        this.value = value;
    }

    Node(T value,Node parentNode){
        this.value = value;
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node node) {
        this.parentNode = node;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        leftChild.setParentNode(this);
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        rightChild.setParentNode(this);
        this.rightChild = rightChild;
    }

    public T getValue() {
        return value;
    }

    public void setAlreadyTransversed(boolean alreadyTransversed) {
        this.alreadyTransversed = alreadyTransversed;
    }

    public boolean isAlreadyTransversed() {
        return alreadyTransversed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}

class BinaryTree<T>{
    Node rootNode;
    LinkedList<Node> nodes = new LinkedList<>(){};
    int levels;
    int totalNodes;

    BinaryTree(Node rootNode){
        this.rootNode = rootNode;
    }

    BinaryTree(T[] values){
        this.totalNodes = values.length;
        findLevels(this.totalNodes);
        constructTree(values);
    }

    private void findLevels(int totalNodes) {
        int level = 0;
        int total = 0;
        while (total < totalNodes) {
            total +=Math.pow(2,level);
            level++;
        }
        this.levels = level;
    }

    public void constructTree(T[] values){
        this.rootNode = new Node(values[0]);
        this.rootNode.setLevel(0);
        constructandConnectBTNodesVertical(values,0,0,this.rootNode);
    }

    private void constructandConnectBTNodesVertical(T[] values, int position, int currentLevel, Node currentNode) {
        currentNode.setLevel(currentLevel);
        System.out.println(currentNode.getValue() + "\t" + currentLevel + "\t" + currentNode.hashCode());
        if (position == values.length-1) return;
        else if (currentLevel< this.levels){
            if (currentNode.getLeftChild()==null) {
                currentLevel++;
                position++;
                currentNode.setLeftChild(new Node(values[position]));
                nodes.add(currentNode.getLeftChild());
                constructandConnectBTNodesVertical(values,position,currentLevel,currentNode.getLeftChild());
            }
            else if (currentNode.getLeftChild() != null && currentNode.getRightChild() == null){
                currentLevel++;
                position++;
                currentNode.setRightChild(new Node(values[position]));
                nodes.add(currentNode.getRightChild());
                constructandConnectBTNodesVertical(values,position,currentLevel,currentNode.getRightChild());
            }
            else if (currentNode.getLeftChild() != null && currentNode.getRightChild() != null){
                currentLevel--;
                constructandConnectBTNodesVertical(values,position,currentLevel,currentNode.getParentNode());
            }
        }
        else if (currentLevel == this.levels){
            currentLevel--;
            constructandConnectBTNodesVertical(values,position,currentLevel,currentNode.getParentNode());
        }
    }

    public Node verticalSearch(int value){
        Node node = verticalSearchTransversal(value,this.rootNode);
        return node;
    }

    private Node verticalSearchTransversal(int value, Node currentNode) {
        if ((currentNode.isAlreadyTransversed()) && currentNode.getLevel() != 0 &&
                (currentNode.getLeftChild() != null && currentNode.getLeftChild().isAlreadyTransversed()) &&
                (currentNode.getRightChild() != null && currentNode.getRightChild().isAlreadyTransversed())){
            return verticalSearchTransversal(value,currentNode.getParentNode());
        }
        currentNode.setAlreadyTransversed(true);
        if (currentNode.getValue().equals(value)){
            resetTree();
            return currentNode;
        }

        else if (currentNode.getLeftChild() == null && currentNode.getRightChild() == null){
            return verticalSearchTransversal(value,currentNode.getParentNode());
        }

        else if(currentNode.getLeftChild() != null && currentNode.getLeftChild().isAlreadyTransversed() == false){
            return verticalSearchTransversal(value,currentNode.getLeftChild());
        }
        else if(currentNode.getLeftChild() != null && (currentNode.getLeftChild().isAlreadyTransversed() == true
                && currentNode.getRightChild().isAlreadyTransversed() == false)){
            return verticalSearchTransversal(value,currentNode.getRightChild());
        }
        else if((currentNode.getLeftChild() != null && currentNode.getRightChild() != null)){
            return verticalSearchTransversal(value,currentNode.getRightChild());
        }


        return currentNode;
    }

    void resetTree(){
        for (int i = 0 ; i < this.nodes.size() ; i++){
            this.nodes.get(i).setAlreadyTransversed(false);
        }
    }

}

public class Main {
    private final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static void main(String[] args) {
        Integer[] array = new Integer[10];
        for (int i = 0 ; i < array.length ; i++){
              array[i] = i*3;
        }
        BinaryTree binaryTree = new BinaryTree(array);

        try{
            Node node = binaryTree.verticalSearch(400);
            System.out.println(node.getValue()+ "\t" + node.getLevel() + "\t" + node.hashCode());
        }
        catch (NullPointerException overflowError){

            logger.log(Level.INFO,overflowError.getMessage());
            System.out.println("Value does not exist in tree");
        }
        finally {
            binaryTree.resetTree();
        }



    }
}
