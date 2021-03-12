package ar.edu.itba;

import java.util.*;

import static ar.edu.itba.App.walls;

public class Graph {

    private List<Node> nodes;

    public Graph() {
        nodes = new LinkedList<>();
    }

    public void gridToGraph(Grid grid, double size, List<Obstacle> obstacles) {

        List<Vector<Double>> points = grid.getPoints();

        for (Vector<Double> point : points) {

            Node node = new Node(point.get(0), point.get(1));

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

    public Graph(Grid gridImpl) {

        nodes = new LinkedList<>();
        int rows = gridImpl.getRows();
        int cols = gridImpl.getCols();
        Double[][][] grid = gridImpl.getGrid();

        for (int row = 0 ; row < rows ; rows++) {

            for (int col = 0 ; col < cols ; cols++) {

                if (grid[row][col] != null) {

                    double x = grid[row][col][0];
                    double y = grid[row][col][1];

                    Node node = new Node(x, y);

                    List<Node> adjacentNodes = getAdjacentNodes(grid, row, col, rows, cols);

                    for (Node other: adjacentNodes) {
                        joinIfNoIntersection(node, other);
                    }

                    nodes.add(node);
                }
            }
        }
    }

    private List<Node> getAdjacentNodes(Double[][][] grid, int row, int col, int rows, int cols) {

        List<Node> adjNodes = new ArrayList<>();

        System.out.println("Row " + row + " of " + rows);
        System.out.println("Col " + col + " of " + cols);

        for (int r = 0 ; r < rows ; r++) {
            for (int c = 0 ; c < cols ; c++) {
                System.out.println("[" + grid[r][c][0] + ", " + grid[r][c][1] + "]");
            }
        }


        // UP
        if ((row + 1) < rows) {
            System.out.println("Accessing: [" + (row + 1) + "] [" + col + "]");
            if (grid[row + 1][col] != null) {

                double x = grid[row + 1][col][0];
                double y = grid[row + 1][col][1];
                Node node = findNodeByCoordinates(x,y);

                if (node != null) {
                    adjNodes.add(node);
                }
            }
        }

        // DOWN
        if ((row - 1) >= 0) {
            System.out.println("Accessing: [" + (row - 1) + "] [" + col + "]");
            if (grid[row - 1][col] != null) {

                double x = grid[row - 1][col][0];
                double y = grid[row - 1][col][1];
                Node node = findNodeByCoordinates(x,y);

                if (node != null) {
                    adjNodes.add(node);
                }
            }
        }

        // LEFT
        if ((col - 1) >= 0) {
            System.out.println("Accessing: [" + row + "] [" + (col - 1) + "]");
            if (grid[row][col - 1] != null) {

                double x = grid[row][col - 1][0];
                double y = grid[row][col - 1][1];
                Node node = findNodeByCoordinates(x,y);

                if (node != null) {
                    adjNodes.add(node);
                }
            }
        }

        // RIGHT
        if ((col + 1) < cols) {
            System.out.println("Accessing: [" + row + "] [" + (col + 1) + "]");
            if (grid[row][col + 1] != null) {

                double x = grid[row][col + 1][0];
                double y = grid[row][col + 1][1];
                Node node = findNodeByCoordinates(x,y);

                if (node != null) {
                    adjNodes.add(node);
                }
            }
        }

        return adjNodes;

    }

    private Node findNodeByCoordinates(double x, double y) {
        return nodes.stream().filter(node -> node.getX() == x && node.getY() == y).findAny().orElse(null);
    }


    private void joinIfNoIntersection(Node node, Node other) {
        Wall edge = new Wall(node.getX(), node.getY(), other.getX(), other.getY());
        boolean intersect = false;
        for (Wall wall: walls) {
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


/*    public Graph(Grid grid) {

        nodes = new LinkedList<>();
        List<Vector<Double>> points = grid.getPoints();

        for (Vector<Double> gridPoint : points) {

            Node node = new Node(gridPoint.get(0), gridPoint.get(1));

            for (Node other: nodes) {

                boolean intersect = false;
                Wall edge = new Wall(gridPoint.get(0), gridPoint.get(1), other.getX(), other.getY());

                for (Wall wall: walls) {

                    if (edge.intersect(wall)) {
                        intersect = true;
                        break;
                    }
                }

                if (!intersect) {
                    node.addNeighbour(other);
                    other.addNeighbour(node);
                }
            }

            nodes.add(node);
        }
    }*/


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
        Set<Node> visited = new HashSet<>();


        for (Node node : nodes) {

            visited.add(node);

            List<Node> path = getRedundantPath(node, visited);

            if (path != null) {
                System.out.println("Found path:");
                removePath(path.get(0), path.get(1), path.get(2));
                break;
            }

        }
    }

    private List<Node> getRedundantPath(Node node1, Set<Node> visited) {
        List<Node> redundantPath = new LinkedList<>();

        System.out.println(visited);

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

        // create 1 <--> 3
        node1.addNeighbour(node3);
        node3.addNeighbour(node1);

    }

    public List<Node> getNodes() {
        return nodes;
    }

}
