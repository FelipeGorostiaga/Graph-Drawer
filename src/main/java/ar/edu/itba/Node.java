package ar.edu.itba;

import java.util.*;

public class Node {
    private Vector<Double> position;
    private Set<Node> neighbours;

    public Node(Vector<Double> position){
        this.position = position;
        neighbours = new HashSet<>();
    }

    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }

    public Vector<Double> getPosition() {
        return position;
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public void printNode(){
        System.out.print(position + " : ");
        for (Node node: neighbours) {
            System.out.print(node.position + ", ");
        }
        System.out.print("\n");
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
