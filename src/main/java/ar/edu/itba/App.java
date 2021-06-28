package ar.edu.itba;


import java.util.List;
import java.util.Vector;

public class App {

    public static Vector<Double> dimensions = new Vector<>(2);
    public static List<Wall> segments;
    public static int scaleFactor;

    public static List<Obstacle> obstacles;

    public static void main(String[] args) {

        CommandParser.parseCommandLine(args);

        segments = FileManager.readFile(dimensions);
        obstacles = Obstacle.defineObstacles(segments);
        scaleFactor = calculateScaleFactor(dimensions);

        Graph graph = new Graph();
        graph.generateGraph();
        FileManager.printGraph();

        Drawer.init();
    }

    private static int calculateScaleFactor(Vector<Double> dimensions) {

/*        double maxDim = Math.max(dimensions.get(0), dimensions.get(1));

        if (maxDim <= 10) return 100;
        if (maxDim <= 100) return 10;*/
        return 20;
    }

}
