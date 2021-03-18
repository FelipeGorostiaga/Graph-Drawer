package ar.edu.itba;


import java.util.List;
import java.util.Vector;

public class App {

    private static final String inFilename = "files/geometry.txt";
    private static final String outFilename = "nodes.xyz";

    // width[0] , height[1]
    public static Vector<Double> dimensions = new Vector<>(2);
    public static List<Wall> segments;
    public static int scaleFactor;
    public static double cellLength;

    public static List<Obstacle> obstacles;

    public static void main(String[] args) {

        segments = FileManager.readFile(inFilename, dimensions);
        obstacles = Obstacle.defineObstacles(segments);
        scaleFactor = calculateScaleFactor(dimensions);
        
        // must be smaller than min length between walls
        //final double cellLength = Grid.calculateCellLength(segments);
        
        cellLength = 5.0;

/*        Grid grid = new Grid(dimensions.get(0), dimensions.get(1), cellLength);
        grid.removePointsInside(obstacles);
        Graph graph = new Graph();
        graph.gridToGraph(grid, cellLength, obstacles);
        graph.trimRedundantNodes();
        ;*/

        List<Node> nodes = Graph.generateGraph();
        FileManager.printGraph(nodes, outFilename);

        System.out.println(nodes.size());

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
