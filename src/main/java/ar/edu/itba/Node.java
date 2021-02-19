package ar.edu.itba;

import java.util.*;

public class Node {
    private Vector<Double> position;
    private Set<Node> neighbours;
    private int id;

    public Node(Vector<Double> position){
        this.position = position;
        neighbours = new HashSet<>();
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public int getId() {
        return id;
    }

    public Vector<Double> getPosition() {
        return position;
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public void printNode(){
        System.out.print(id);
        for (Node node: neighbours) {
            System.out.print(" " + node.getId());
        }
        System.out.print("\n");
    }

    public void setId(int id){
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return position.equals(node.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
