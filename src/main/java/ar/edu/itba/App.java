package ar.edu.itba;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class App {
    public static void main(String[] args) {

        final Vector<Double> dimensions = new Vector<>(2);
        List<Wall> segments = new ArrayList<>();
        final int cell_size = 2;

//        File file = new File("");
//        Scanner sc = null;
//
//        try {
//            sc = new Scanner(file).useLocale(Locale.US);
//        } catch (FileNotFoundException e) {
//            System.out.println("Failed to read files...");
//            System.exit(1);
//        }
//
//        //Dimensions
//        dimensions.add(sc.nextDouble());
//        dimensions.add(sc.nextDouble());
//
//        // Segments:
//        // 0 - wall type
//        // 1 - shelf
//        // 2 - counter
//        while (sc.hasNextLine()){
//            int type = sc.nextInt();
//            Wall wall = new Wall(sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), type);
//            segments.add(wall);
//        }

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

    }

    private List<Obstacle> defineObstacles(List<Wall> segments){
        List<Obstacle> obstacles = new LinkedList<>();
        boolean found = false;

        for (Wall segment : segments) {
            for (int j = 0; j < obstacles.size() && !found; j++) {
                Obstacle obstacle = obstacles.get(j);
                found = obstacle.addCorner(segment);
            }
            if (!found) {
                Obstacle obstacle = new Obstacle();
                obstacle.addSegment(segment);
                obstacles.add(obstacle);
            }
        }

        return obstacles;
    }


}
