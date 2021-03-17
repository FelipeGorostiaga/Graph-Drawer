package ar.edu.itba;


import java.util.List;
import java.util.Vector;

public class App {

    private static final String inFilename = "files/text.txt";
    private static final String outFilename = "nodes.xyz";

    // width[0] , height[1]
    public static Vector<Double> dimensions = new Vector<>(2);
    public static List<Wall> segments;
    public static int scaleFactor;

    public static List<Obstacle> obstacles;

    public static void main(String[] args) {

        segments = FileManager.readFile(inFilename, dimensions);
        obstacles = Obstacle.defineObstacles(segments);
        scaleFactor = calculateScaleFactor(dimensions);
        
        // must be smaller than min length between walls
        //final double cellLength = Grid.calculateCellLength(segments);
        
        final double cellLength = 2.0;

        // create grid of nodes
        Grid grid = new Grid(dimensions.get(0), dimensions.get(1), cellLength);

        // remove invalid nodes inside obstacles
        grid.removePointsInside(obstacles);

        // convert grid to graph of nodes
        Graph graph = new Graph();
        graph.gridToGraph(grid, cellLength, obstacles);

        // trim useless nodes
        graph.trimRedundantNodes();

        // add nodes to file
        FileManager.printGraph(graph, outFilename);

        // draw geometry + graph
        Drawer.init();
    }

    private static int calculateScaleFactor(Vector<Double> dimensions) {

        java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
        double maxDim = Math.max(dimensions.get(0), dimensions.get(1));

        if (maxDim <= 10) return 100;
        if (maxDim <= 100) return 10;
        return 1;
    }

}
