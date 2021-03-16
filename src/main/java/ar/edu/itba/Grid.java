package ar.edu.itba;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import static ar.edu.itba.App.segments;



public class Grid {

    private List<Vector<Double>> points;

    private final double height;
    private final double width;
    private final double size;

    public Grid(double width, double height, double size) {

        points = new LinkedList<>();
        for(double x = size; x < width; x += size){
            for(double y = size; y < height; y += size ){
                Vector<Double> point = new Vector<>(2);
                point.add(x);
                point.add(y);
                points.add(point);
            }
        }

        this.height = height;
        this.width = width;
        this.size = size;
    }

    public static boolean areReachableNodes(Node node, Node other) {
        Wall nodeWall = new Wall(node.getX(), node.getY(), other.getX(), other.getY());
        for (Wall wall: segments) {
            if (wall.intersect(nodeWall)) {
                return false;
            }
        }
        return true;
    }


    public void removePointsInside(final List<Obstacle> obstacles){

        List<Vector<Double>> aux = new LinkedList<>();
        for (Obstacle obstacle: obstacles){
            boolean isInside = false;
            for (Vector<Double> point: points){
                if(obstacle.isInside(point, width)){
                    aux.add(point);
                }
            }
        }

        points.removeAll(aux);
        System.out.println("Removed " + aux.size() + " points inside obstacles");
    }

    public List<Vector<Double>> getPoints() {
        return points;
    }

    public void setPoints(List<Vector<Double>> points) {
        this.points = points;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getSize() {
        return size;
    }

    public static double calcultateDistance(Vector<Double> p1, Vector<Double> p2) {
        return Math.sqrt(Math.pow(p2.get(0) - p1.get(0), 2) + Math.pow(p2.get(1) - p1.get(1), 2));
    }

    public static double calculateCellLength(List<Wall> walls) {

        double minLength = Double.POSITIVE_INFINITY;

        for (Wall wall : walls) {

            final Vector<Double> p1 = wall.getP1();
            final Vector<Double> p2 = wall.getP1();

            for (Wall other : walls) {

                if (!wall.equals(other)) {

                    final Vector<Double> otherP1 = other.getP1();
                    final Vector<Double> otherP2 = other.getP2();

                    final double p1Distance = Grid.calcultateDistance(p1, otherP1);
                    final double p2Distance = Grid.calcultateDistance(p2, otherP2);
                    final double p12Distance = Grid.calcultateDistance(p1, otherP2);
                    final double p21Distance = Grid.calcultateDistance(otherP1, p2);

                    if (p1Distance != 0 && p1Distance < minLength) {
                        minLength = p1Distance;
                    }
                    if (p2Distance != 0 && p2Distance < minLength) {
                        minLength = p2Distance;
                    }
                    if (p12Distance != 0 && p12Distance < minLength) {
                        minLength = p12Distance;
                    }
                    if (p21Distance != 0 && p21Distance < minLength) {
                        minLength = p21Distance;
                    }
                }
            }
        }
        return minLength / 2;
    }

}

