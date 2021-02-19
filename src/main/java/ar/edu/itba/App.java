package ar.edu.itba;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class App {
    public static void main(String[] args) {

        final Vector<Double> dimensions = new Vector<>(2);
        List<Wall> segments;
        List<Obstacle> obstacles;




        if(args.length < 2){
            System.exit(1);
        }

        System.out.println(args[0]);
        final double cell_size = Double.parseDouble(args[0]);

        String  inFile = args[1];
        String outFile;
        if(args.length == 4){
            outFile = args[2];
        }else {
            outFile = "";
        }

        System.out.println(inFile);
        System.out.println(outFile);

        segments = FileManager.readFile(inFile, dimensions);

        obstacles = Obstacle.defineObstacles(segments);

        Grid grid = new Grid(dimensions.get(1), dimensions.get(0), cell_size);

        grid.removePointsInside(obstacles);

        Graph graph = new Graph();

        graph.gridToGraph(grid, cell_size, obstacles);

        graph.addIdToNodes();

        FileManager.printGraph(graph, outFile);






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
}
