package ar.edu.itba;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Graph {
    private List<Node> nodes;

    public Graph(){
        nodes = new LinkedList<>();
    }

    public void gridToGraph(Grid grid, double size, List<Obstacle> obstacles){
        List<Vector<Double>> points = grid.getPoints();

        for(Vector<Double> point: points){
            Node node = new Node(point.get(0), point.get(1));
            for (Node other: nodes){
                if((Math.abs(point.get(0) - other.getX()) == size
                        && point.get(1).equals(other.getY()))
                        || (Math.abs(point.get(1) - other.getY()) == size
                        && point.get(0).equals(other.getX()))){
                    Wall edge = new Wall(point.get(0), point.get(1), other.getX(), other.getY());
                    boolean intersect = false;
                    for (int i = 0; i < obstacles.size() && !intersect ; i++){
                        List<Wall> walls = obstacles.get(i).getSegments();
                        for (int j = 0; j < walls.size() && !intersect; j++){
                            if (walls.get(j).intersect(edge)){
                                intersect = true;
                            }
                        }
                    }
                    if(!intersect){
                        node.addNeighbour(other);
                        other.addNeighbour(node);
                    }
                }
            }
            nodes.add(node);
        }


    }

    public void addIdToNodes(){
        int i = 0;
        for(Node node: nodes){
            node.setId(i);
            i++;
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }


}
