package ar.edu.itba;

import java.util.List;
import java.util.Vector;

public class App {

    private static final String inFilename = "files/text.txt";
    private static final String outFilename = "graph-nodes.xyz";

    // width, height
    static Vector<Double> dimensions = new Vector<>(2);
    static List<Wall> walls;


    static List<Obstacle> obstacles;

    public static void main(String[] args) {

        walls = FileManager.readFile(inFilename, dimensions);
        obstacles = Obstacle.defineObstacles(walls);

        // must be smaller than min length between walls
        final double cellLength = Grid.calculateCellLength(walls);

        System.out.println("Cell length: " + cellLength);

        // create grid of nodes
        Grid grid = new Grid(dimensions.get(0), dimensions.get(1), cellLength);

        // remove invalid nodes inside obstacles
        grid.removePointsInside(obstacles);

        // convert grid to graph of nodes
        /*Graph graph = new Graph();
        graph.gridToGraph(grid, cellLength, obstacles);*/
        Graph graph = new Graph(grid);
        graph.addIdToNodes();
        //graph.trimRedundantNodes();

        FileManager.printGraph(graph, outFilename);
    }
}
