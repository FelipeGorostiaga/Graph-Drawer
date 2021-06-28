package ar.edu.itba;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Obstacle {
    private List<Wall> segments;

    private static enum dir {down, left, up}

    public Obstacle() {
        segments = new LinkedList<>();
    }

    public List<Wall> getSegments() {
        return segments;
    }

    /* Sees if segment is part of obstacle,
       if it is part it is added and returns true,
       else returns false
    * */
    public boolean isPart(Wall s1) {
        Vector<Double> p1 = s1.getP1();
        Vector<Double> p2 = s1.getP2();

        for (Wall segment : segments) {
            if (segment.getP1().get(0).equals(p1.get(0)) && segment.getP1().get(1).equals(p1.get(1))) {
                return true;
            }
            if (segment.getP1().get(0).equals(p2.get(0)) && segment.getP1().get(1).equals(p2.get(1))) {
                return true;
            }
            if (segment.getP2().get(0).equals(p1.get(0)) && segment.getP2().get(1).equals(p1.get(1))) {
                return true;
            }
            if (segment.getP2().get(0).equals(p2.get(0)) && segment.getP2().get(1).equals(p2.get(1))) {
                return true;
            }

        }
        return false;
    }

    public void addSegment(Wall segment) {
        segments.add(segment);
    }

    public boolean isInside(Vector<Double> point, Double end) {

        //Obstacle cant have nothing inside with less than 3 segments
        if (segments.size() < 3) {
            return false;
        }

        return flux(point, dir.down, end) % 2 == 1 && flux(point, dir.left, end) % 2 == 1 && flux(point, dir.up, end) == 1;

    }

    private int flux(Vector<Double> point, dir dir, double end) {
        Wall other;
        switch (dir) {
            case up:
                other = new Wall(point.get(0), point.get(1), point.get(0), end);
                break;
            case down:
                other = new Wall(point.get(0), point.get(1), point.get(0), 0);
                break;
            case left:
                other = new Wall(point.get(0), point.get(1), 0, point.get(1));
                break;
            default:
                throw new IllegalArgumentException("Invalid (flux) direction");
        }

        int corners = 0;
        int count = 0;

        for (Wall segment : segments) {
            if (other.inWall(segment.getP1()) || other.inWall(segment.getP2())) {
                corners++;
            }
            if (segment.inWall(point)) return 1;
            if (segment.intersect(other)) {
                count++;
            }
        }

        corners = corners / 2;
        return count - corners;
    }

    public static List<Obstacle> defineObstacles(List<Wall> segments) {
        List<Obstacle> obstacles = new LinkedList<>();
        List<Obstacle> aux = new LinkedList<>();
        for (Wall segment : segments) {
            if (segment.getType() != 0) {
                for (int j = 0; j < obstacles.size(); j++) {
                    Obstacle obstacle = obstacles.get(j);
                    if (obstacle.isPart(segment))
                        aux.add(obstacle);

                }

                if (aux.size() == 0) {
                    Obstacle obstacle = new Obstacle();
                    obstacle.addSegment(segment);
                    obstacles.add(obstacle);
                } else {
                    aux.get(0).addSegment(segment);
                    if (aux.size() > 1) {
                        obstacles.removeAll(aux);
                        obstacles.add(Obstacle.unify(aux));
                    }

                }
            } else {
                Obstacle obstacle = new Obstacle();
                obstacle.addSegment(segment);
                obstacles.add(obstacle);
            }

            aux.clear();


        }

        return obstacles;
    }

    public static Obstacle unify(List<Obstacle> obstacles) {
        Obstacle obstacle = new Obstacle();

        for (Obstacle obs : obstacles) {
            for (Wall segment : obs.getSegments()) {
                obstacle.addSegment(segment);
            }

        }

        return obstacle;
    }
}
