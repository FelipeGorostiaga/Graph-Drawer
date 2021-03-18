package ar.edu.itba;

import java.util.*;

public class Node {

    private int id;
    private double x;
    private double y;
    private final Set<Node> neighbours;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        neighbours = new HashSet<>();
    }

    public Node(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    public void removeNeighbour(Node neighbour) {
        neighbours.remove(neighbour);
    }

    public void printNode() {
        System.out.print("Node " + this.id + " --> [");
        for (Node node : neighbours) {
            System.out.print(" " + node.getId());
        }
        System.out.print(" ]\n");
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
