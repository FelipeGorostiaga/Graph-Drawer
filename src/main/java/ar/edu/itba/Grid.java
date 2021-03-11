package ar.edu.itba;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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



    public void removePointsInside(final List<Obstacle> obstacles){
//        for (Vector<Double> point : points) {
//            boolean isInside = false;
//            for (Obstacle obstacle : obstacles) {
//                if(obstacle.isInside(point, width)){
//                    isInside = true;
//                    break;
//                }
//            }
//            if (isInside){
//                points.remove(point);
//            }
//        }
        List<Vector<Double>> aux = new LinkedList<>();
        for (Obstacle obstacle: obstacles){
            boolean isInside = false;
            for (Vector<Double> point: points){
                if(obstacle.isInside(point, width)){
                    aux.add(point);
                }
            }

//            points.stream().filter(point -> obstacle.isInside(point, width)).forEach(point -> points.remove(point));

        }

        points.removeAll(aux);
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
}

