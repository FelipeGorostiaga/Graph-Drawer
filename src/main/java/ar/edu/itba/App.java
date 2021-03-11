package ar.edu.itba;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class App {

    private static final String inFilename = "files/geometry.txt";
    private static final String outFilename = "graph-nodes.xyz";

    // width, height
    static Vector<Double> dimensions = new Vector<>(2);
    static List<Wall> walls;


    static List<Obstacle> obstacles;

    public static void main(String[] args) {

        walls = FileManager.readFile(inFilename, dimensions);
        obstacles = Obstacle.defineObstacles(walls);

        // must be smaller than min length between walls
        final double cellLength = calculateCellLength(walls);

        System.out.println(cellLength);

        System.out.println("creating grid");
        System.out.println("dim" + dimensions);

        // create grid of nodes
        Grid grid = new Grid(dimensions.get(0), dimensions.get(1), cellLength);

        System.out.println("created grid!");

        // remove invalid nodes inside obstacles
        grid.removePointsInside(obstacles);

        // convert grid to graph of nodes
        Graph graph = new Graph();
        graph.gridToGraph(grid, cellLength, obstacles);
        graph.addIdToNodes();

        FileManager.printGraph(graph, outFilename);
    }

    private static double calculateCellLength(List<Wall> walls) {

        double minLength = Double.POSITIVE_INFINITY;

        for (Wall wall : walls) {

            final Vector<Double> center = wall.getCenter();
            final Vector<Double> p1 = wall.getP1();
            final Vector<Double> p2 = wall.getP1();

            for (Wall other : walls) {

                // don't calculate distance with itself
                if (!wall.equals(other)) {

                    final Vector<Double> otherCenter = other.getCenter();
                    final Vector<Double> otherP1 = other.getP1();
                    final Vector<Double> otherP2 = other.getP2();

                    final double centerDistance = Grid.calcultateDistance(center, otherCenter);
                    final double p1Distance = Grid.calcultateDistance(p1, otherP1);
                    final double p2Distance = Grid.calcultateDistance(p2, otherP2);
                    final double p12Distance = Grid.calcultateDistance(p1, otherP2);

                    if (centerDistance != 0 && centerDistance < minLength) {
                        minLength = centerDistance;
                    }
                    if (p1Distance != 0 && p1Distance < minLength) {
                        minLength = p1Distance;
                    }
                    if (p2Distance != 0 && p2Distance < minLength) {
                        minLength = p2Distance;
                    }
                    if (p12Distance != 0 && p12Distance < minLength) {
                        minLength = p12Distance;
                    }
                }
            }
        }
        return minLength;
    }
}


//        Obstacle obstacle = new Obstacle();
//        Wall wall1 = new Wall(0,5,4,7);
//        Wall wall2 = new Wall(4,7,6,5);
//        Wall wall3 = new Wall(6,5,4,3);
//        Wall wall4 = new Wall(4,3,0,5);
//
//        obstacle.addSegment(wall1);
//        obstacle.addSegment(wall2);
//        obstacle.addSegment(wall3);
//        obstacle.addSegment(wall4);
//
//        Grid grid = new Grid(10, 10, 2);
//
//        List<Obstacle> obstacles = new LinkedList<>();
//        obstacles.add(obstacle);
//
//        grid.removePointsInside(obstacles);
//
//        Graph graph = new Graph();
//
//        graph.gridToGraph(grid, cell_size, obstacles);
//
//        System.out.println(graph.getNodes().size());
//
//        for (Vector<Double> point : grid.getPoints()){
//            System.out.println(point);
//        }
//
//        System.out.println("\n");
//
//        for(Node node: graph.getNodes()){
//            node.printNode();
//        }
//
