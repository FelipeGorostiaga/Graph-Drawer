package ar.edu.itba;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String NODES_FILE = "nodosSuper.txt";

    private FileManager(){
    }

    public static List<Wall> readFile(String filename, Vector<Double> dimensions){
        File file = new File(filename);
        Scanner sc = null;

        try {
            sc = new Scanner(file).useLocale(Locale.US);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read files...");
            System.exit(1);
        }

        //Dimensions
        dimensions.add(sc.nextDouble());
        dimensions.add(sc.nextDouble());

        List<Wall> segments = new ArrayList<>();

        // Segments:
        // 0 - wall type
        // 1 - shelf
        // 2 - counter
        while (sc.hasNextLine()){
            int type = sc.nextInt();
            Wall wall = new Wall(sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), type);
            segments.add(wall);
        }

        return segments;
    }

    public static void printGraph(Graph graph, String filename){
        List<Node> nodes = graph.getNodes();

        if(filename.equals("")){
            filename = NODES_FILE;
        }

        File file = new File(filename);

        try {
            if(file.createNewFile()){
                System.out.println("File created: " + file.getName());
            }else{
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            System.setOut(new PrintStream(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(Node node: nodes){
            System.out.println(node.getId() + " " + node.getPosition().get(0) + " " + node.getPosition().get(1));
        }

        System.out.println("\n");

        for(Node node: nodes){
            node.printNode();
        }

    }
}
