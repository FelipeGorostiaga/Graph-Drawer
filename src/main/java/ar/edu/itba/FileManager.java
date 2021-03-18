package ar.edu.itba;

import java.io.*;
import java.util.*;

import static ar.edu.itba.Graph.nodes;

public class FileManager {

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

        dimensions.add(sc.nextDouble());
        dimensions.add(sc.nextDouble());

        List<Wall> walls = new ArrayList<>();

        while (sc.hasNextLine()){
            try {
                int type = sc.nextInt();
                Wall wall = new Wall(sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), sc.nextDouble(), type);
                walls.add(wall);
            }
            catch (Exception e) {
                break;
            }
        }
        return walls;
    }

    public static void printGraph(List<Node> nodes, String filename){


        File file = new File(filename);

        try {
            if(file.createNewFile()){
                System.out.println("File created: " + file.getName());
            }else{
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred...");
            e.printStackTrace();
        }

        try {
            System.setOut(new PrintStream(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(Node node: nodes){
            System.out.println(node.getId() + " " + node.getX() + " " + node.getY());
        }
        System.out.println("\n");
        for(Node node: nodes){
            node.printNode();
        }

    }
}
