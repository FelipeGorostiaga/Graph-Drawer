package ar.edu.itba;

import java.util.*;

import static ar.edu.itba.App.segments;

public class Graph {

    public static List<Node> nodes;

    public Graph() {
        nodes = new LinkedList<>();
    }

    public void gridToGraph(Grid grid, double size, List<Obstacle> obstacles) {

        List<Vector<Double>> points = grid.getPoints();

        int id = 0;
        for (Vector<Double> point : points) {

            Node node = new Node(point.get(0), point.get(1), id++);

            for (Node other : nodes) {

                if ((Math.abs(point.get(0) - other.getX()) == size
                        && point.get(1).equals(other.getY()))
                        || (Math.abs(point.get(1) - other.getY()) == size
                        && point.get(0).equals(other.getX()))) {

                    Wall edge = new Wall(point.get(0), point.get(1), other.getX(), other.getY());

                    boolean intersect = false;

                    for (int i = 0; i < obstacles.size() && !intersect; i++) {

                        List<Wall> walls = obstacles.get(i).getSegments();

                        for (int j = 0; j < walls.size() && !intersect; j++) {
                            if (walls.get(j).intersect(edge)) {
                                intersect = true;
                            }
                        }
                    }
                    if (!intersect) {
                        node.addNeighbour(other);
                        other.addNeighbour(node);
                    }
                }
            }

            nodes.add(node);
        }
    }



    private Node findNodeByCoordinates(double x, double y) {
        return nodes.stream().filter(node -> node.getX() == x && node.getY() == y).findAny().orElse(null);
    }


    private void joinIfNoIntersection(Node node, Node other) {
        Wall edge = new Wall(node.getX(), node.getY(), other.getX(), other.getY());
        boolean intersect = false;
        for (Wall wall: segments) {
            if (edge.intersect(wall)) {
                intersect = true;
                break;
            }
        }
        if (!intersect) {
            System.out.println("Adding edges between nodes " + node.getId() + " and " + other.getId());
            node.addNeighbour(other);
            other.addNeighbour(node);
        }
    }


    public void addIdToNodes() {
        int i = 0;
        for (Node node : nodes) {
            node.setId(i);
            i++;
        }
    }

    public void trimRedundantNodes() {

        // 1 <-> 2 <-> 3 and THEN 1 <-> 3 (if no obstacle between 1 <-> 3)

        boolean removed = false;
        Set<Node> visited;
        boolean trimmed;

        for (int i = 0 ; i < 100000 ; i++) {

            visited = new HashSet<>();

            for (Node node : nodes) {

                visited.add(node);

                List<Node> path = getRedundantPath(node, visited);

                if (path != null) {
                    removePath(path.get(0), path.get(1), path.get(2));
                    break;
                }
            }

        }
        removeUnreachableNodes();
        addIdToNodes();
    }

    private void removeUnreachableNodes() {
        List<Node> removeList = new LinkedList<>();

        for (Node node: nodes) {
            if (node.getNeighbours().isEmpty()) {
                removeList.add(node);
            }
        }
        nodes.removeAll(removeList);

    }

    private List<Node> getRedundantPath(Node node1, Set<Node> visited) {

        List<Node> redundantPath = new LinkedList<>();

        // Go to neighbour
        for (Node node2 : node1.getNeighbours()) {

            for (Node node3 : node2.getNeighbours()) {

                // prevent triangle 1 <--> 2 <--> 1
                if (!node3.equals(node1)) {

                    if (!visited.contains(node3)) {

                        visited.add(node3);

                        if (Grid.areReachableNodes(node1, node3)) {
                            redundantPath.add(node1);
                            redundantPath.add(node2);
                            redundantPath.add(node3);
                            return redundantPath;
                        }
                    }


                }

            }

        }
        return null;
    }

    private void removePath(Node node1, Node node2, Node node3) {

        System.out.println("Removing redundant path:");
        System.out.println(node1.getId() + " <--> " + node2.getId() + " <--> " + node3.getId());
        System.out.println(node1.getId() + " <--> " + node3.getId());

        // remove 1 <--X--> 2
        node1.getNeighbours().remove(node2);
        node2.getNeighbours().remove(node1);

        // remove 2 <--X--> 3
        node3.getNeighbours().remove(node2);
        node2.getNeighbours().remove(node3);

        // create 1 <--> 3
        node1.addNeighbour(node3);
        node3.addNeighbour(node1);

    }

    public List<Node> getNodes() {
        return nodes;
    }

}
