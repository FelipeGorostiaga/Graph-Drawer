package ar.edu.itba;

import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.App.segments;
import static ar.edu.itba.CommandParser.cellLength;

public class Graph {

    public static List<Node> nodes;

    public static List<Quadrant> quadrants;

    private static final int MAX_NEIGHBOURS = 4;

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

    public static void addIdToNodes() {
        int i = 0;
        for (Node node : nodes) {
            node.setId(i);
            i++;
        }
    }

    public static void trimRedundantNodes() {

        // 1 <-> 2 <-> 3 and THEN 1 <-> 3 (if no obstacle between 1 <-> 3)

        boolean removed = true;
        Set<Node> visited;
        List<Node> path = null;

        for (int i = 0; i < 10000; i++) {

            for (Node node : nodes) {
                visited = new HashSet<>();
                visited.add(node);

                path = getRedundantPath(node, visited);

                if (path != null) {
                    removePath(path.get(0), path.get(1), path.get(2));
                    removed = true;
                    break;
                }
            }

            if (path == null) {
                removed = false;
            }

        }
        removeUnreachableNodes();
        addIdToNodes();
    }

    // remove nodes with no edges
    private static void removeUnreachableNodes() {
        List<Node> removeList = new LinkedList<>();

        for (Node node : nodes) {
            if (node.getNeighbours().isEmpty()) {
                removeList.add(node);
            }
        }
        nodes.removeAll(removeList);
    }

    private static List<Node> getRedundantPath(Node node1, Set<Node> visited) {

        List<Node> redundantPath = new LinkedList<>();

        // Go to neighbour
        for (Node node2 : node1.getNeighbours()) {

            if (node2.getNeighbours().size() == 2) {

                // 2-jump neighbour
                for (Node node3 : node2.getNeighbours()) {

                    // prevent cycle 1 <--> 2 <--> 1
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
        }
        return null;
    }

    private static void removePath(Node node1, Node node2, Node node3) {

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

    private static List<Quadrant> getQuadrants() {

        List<Double> yLimits = segments.stream()
                .map(wall -> wall.getP1().get(1))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<Quadrant> quads = new ArrayList<>();

        for (int i = 0; i < yLimits.size() - 1; i++) {
            double yMin = yLimits.get(i);
            double yMax = yLimits.get(i + 1);
            List<Double> xLimits = new ArrayList<>();

            List<Wall> tempWalls = segments.stream()
                    .filter(wall -> (Double.compare(Math.min(wall.getP1().get(1), wall.getP2().get(1)), yMin) <= 0
                            && Double.compare(Math.max(wall.getP1().get(1), wall.getP2().get(1)), yMax) >= 0)
                            || (Double.compare(Math.min(wall.getP1().get(1), wall.getP2().get(1)), yMin) == 0)
                            || (Double.compare(Math.max(wall.getP1().get(1), wall.getP2().get(1)), yMax) == 0)
                            || (Double.compare(Math.min(wall.getP1().get(1), wall.getP2().get(1)), yMax) == 0)
                            || (Double.compare(Math.max(wall.getP1().get(1), wall.getP2().get(1)), yMin) == 0)).collect(Collectors.toList());

            for (Wall w : tempWalls) {
                if (!xLimits.contains(w.getP1().get(0)))
                    xLimits.add(w.getP1().get(0));
                if (!xLimits.contains(w.getP2().get(0)))
                    xLimits.add(w.getP2().get(0));
            }
            xLimits.sort((x, y) -> (int) Math.signum(x - y));
            for (int j = 0; j < xLimits.size() - 1; j++) {
                quads.add(new Quadrant(xLimits.get(j), xLimits.get(j + 1), yLimits.get(i), yLimits.get(i + 1), cellLength));
            }
        }
        return quads;
    }

    // Se unen todos los nodos dentro de cada cuadrante, resultando en un grafo conexo por cuadrante
    private static void joinNodes(List<Quadrant> quadrants) {
        LinkedList<Markable<Quadrant>> quads = new LinkedList<>();
        quads.addAll(quadrants.stream().map(Markable::new).collect(Collectors.toList()));
        quads.getFirst().setMarked(true);
        while (!quads.isEmpty()) {
            Markable<Quadrant> curr = quads.removeFirst();
            quads.stream()
                    // solo los vecinos del nodo
                    .filter(x -> curr.getEntity().neighbours(x.getEntity()))
                    .forEach(x -> {
                        double minDist = Double.POSITIVE_INFINITY;
                        Node bNode = null, otherNode = null;
                        for (Node currNode : curr.getEntity().getNodes()) {
                            for (Node xNode : x.getEntity().getNodes()) {
                                if (Grid.areReachableNodes(currNode, xNode)
                                        && Grid.distance(currNode, xNode) < minDist) {
                                    minDist = Grid.distance(currNode, xNode);
                                    bNode = currNode;
                                    otherNode = xNode;
                                }
                            }
                        }
                        if (bNode != null && otherNode != null) {
                            if (!bNode.getNeighbours().contains(otherNode))
                                bNode.addNeighbour(otherNode);
                            if (!otherNode.getNeighbours().contains(bNode))
                                otherNode.addNeighbour(bNode);
                        }
                        x.setMarked(true);
                    });

        }
    }

    private static void removeRedundantEdges(List<Node> nodes) {
        for (Node node : nodes) {
            if (node.getNeighbours().size() > MAX_NEIGHBOURS) {
                for (Node neigh : node.getNeighbours()) {
                    if (!neigh.getNeighbours().contains(node))
                        continue;
                    Set<Node> intersection = new HashSet<>(node.getNeighbours());
                    intersection.retainAll(neigh.getNeighbours());

                    for (Node sharedNeigh : intersection) {
                        if (Grid.distance(sharedNeigh, node) >
                                Grid.distance(sharedNeigh, neigh))
                            sharedNeigh.removeNeighbour(node);
                    }
                }
                node.getNeighbours().removeAll(
                        node.getNeighbours().stream()
                                .filter(n -> !n.getNeighbours().contains(node)).collect(Collectors.toList()));
            }
        }
    }

    // Genera un grafo de cantidad minimal de nodos
    public void generateGraph() {
        // Generate quadrants
        quadrants = getQuadrants();
        // Join nodes from different quadrants
        joinNodes(quadrants);
        quadrants.forEach(x -> nodes.addAll(x.getNodes()));
        addIdToNodes();
        removeRedundantEdges(nodes);
        // Eliminate nodes that have no neighbours
        List<Node> emptyNodes = nodes.stream().filter(n -> n.getNeighbours().size() == 0).collect(Collectors.toList());
        nodes.removeAll(emptyNodes);
    }

    // Clase helper para poder saber que cuadrantes ya fueron transitados
    private static class Markable<T> {
        private boolean marked = false;
        private T entity;

        Markable(T entity) {
            this.setEntity(entity);
        }

        public T getEntity() {
            return entity;
        }

        public void setEntity(T entity) {
            this.entity = entity;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMarked(boolean marked) {
            this.marked = marked;
        }

    }

}
