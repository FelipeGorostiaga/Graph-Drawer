package ar.edu.itba;

import java.util.*;

public class Node {

    private int id;
    private double x;
    private double y;
    private final Set<Node> neighbours;

    public Node(double x, double y){
        this.x = x;
        this.y = y;
        neighbours = new HashSet<>();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public void printNode(){
        System.out.print("Node " + this.id);
        System.out.println("Neighbours:");
        for (Node node: neighbours) {
            System.out.print(" " + node.getId());
        }
        System.out.print("\n");
    }

}
