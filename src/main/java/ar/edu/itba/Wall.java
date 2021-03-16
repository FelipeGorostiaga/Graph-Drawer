package ar.edu.itba;

import java.util.Vector;

public class Wall {

    // x1 y1
    private final Vector<Double> p1;
    // x2 y2
    private final Vector<Double> p2;

    private int type;

    public Wall(double x1, double y1, double x2, double y2, int type) {
        p1 = new Vector<>(2);
        p2 = new Vector<>(2);
        p1.add(x1);
        p1.add(y1);
        p2.add(x2);
        p2.add(y2);
        this.type = type;
    }

    public Wall(double x1, double y1, double x2, double y2) {
        p1 = new Vector<>(2);
        p2 = new Vector<>(2);
        p1.add(x1);
        p1.add(y1);
        p2.add(x2);
        p2.add(y2);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Vector<Double> getP1() {
        return p1;
    }

    public Vector<Double> getP2() {
        return p2;
    }

    public boolean intersect(Wall other){
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, p2, other.getP1());
        int o2 = orientation(p1, p2, other.getP2());
        int o3 = orientation(other.getP1(), other.getP2(), p1);
        int o4 = orientation(other.getP1(), other.getP2(), p2);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;

        }

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, other.getP1(), p2)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, other.getP2(), p2)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(other.getP1(), p1, other.getP2())) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(other.getP1(), p2, other.getP2())) return true;

        return false; // Doesn't fall in any of the above cases
    }

    //Given three collinear points p,q,r, the function checks if
    //point q lies on line segment 'pr'
    private boolean onSegment(Vector<Double> p, Vector<Double> q, Vector<Double> r){
        if (q.get(0) <= Math.max(p.get(0), r.get(0)) && q.get(0) >= Math.min(p.get(0), r.get(0)) &&
                q.get(1) <= Math.max(p.get(1), r.get(1)) && q.get(1) >= Math.min(p.get(1), r.get(1))){
            return true;
        }
        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private int orientation(Vector<Double> p, Vector<Double> q, Vector<Double> r){
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        double val = (q.get(1) - p.get(1)) * (r.get(0) - q.get(0)) -
                (q.get(0) - p.get(0)) * (r.get(1) - q.get(1));

        if (val == 0) return 0;

        return (val > 0)? 1: 2;
    }

    public Vector<Double> getCenter() {
        final Vector<Double> wallCenter = new Vector<>(2);
        double x = Math.abs((p1.get(0) - p2.get(0)) / 2);
        double y = Math.abs((p1.get(1) - p2.get(1)) / 2);
        wallCenter.add(x);
        wallCenter.add(y);
        return wallCenter;
    }

    public boolean inWall(Vector<Double> point){
        return distance(p1 ,point) + distance(point, p2) == distance(p1, p2);
    }

    private double distance(Vector<Double> a, Vector<Double> b){
        double x = Math.pow(a.get(0) - b.get(0), 2);
        double y = Math.pow(a.get(1) - b.get(1), 2);
        return Math.sqrt(x + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wall)) return false;
        Wall wall = (Wall) o;
        return (p1.get(0).equals(wall.p2.get(0))) &&
                (p1.get(1).equals(wall.p2.get(1)));
    }

    @Override
    public int hashCode() {
        int result = p1.hashCode();
        result = 31 * result + p2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "p1=" + p1.get(0) + " " + p1.get(1) +
                ", p2=" + p2.get(0) + " " + p2.get(1) +
                '}';
    }

    //    public boolean isParallel(Wall other){
//        boolean bool = false;
//        if (p1.get(0).equals(p2.get(0)) && other.getP1().get(0).equals(other.getP2().get(0))) {
//            return true;
//        }
//        double m1 = (p1.get(1) - p2.get(1))/(p1.get(0) - p2.get(0));
//        double m2 = (other.getP2().get(1) - other.getP2().get(1))/(other.getP1().get(0) - other.getP2().get(0));
//        return m1 == m2;
//
//    }
}
