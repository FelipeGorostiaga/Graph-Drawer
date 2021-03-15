package ar.edu.itba;

import java.util.List;
import java.util.Vector;

public class App {

    private static final String inFilename = "files/text.txt";
    private static final String outFilename = "graph-nodes.xyz";

    // width 0 , height 1
    public static Vector<Double> dimensions = new Vector<>(2);
    public static List<Wall> segments;

    public static List<Obstacle> obstacles;

    public static void main(String[] args) {

        segments = FileManager.readFile(inFilename, dimensions);
        obstacles = Obstacle.defineObstacles(segments);

        // must be smaller than min length between walls
        final double cellLength = Grid.calculateCellLength(segments);


        // create grid of nodes
        Grid grid = new Grid(dimensions.get(0), dimensions.get(1), cellLength);

        // remove invalid nodes inside obstacles
        grid.removePointsInside(obstacles);

        // convert grid to graph of nodes
        Graph graph = new Graph();
        graph.gridToGraph(grid, cellLength, obstacles);

        // trim useless nodes
        //graph.trimRedundantNodes();

        FileManager.printGraph(graph, outFilename);
    }
}
