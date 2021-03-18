package ar.edu.itba;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Quadrant {

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private Collection<Node> nodes = new HashSet<>();

    public Quadrant(double xMin, double xMax, double yMin, double yMax, List<Wall> walls, double ind) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        int xSteps = (int) Math.floor((xMax-xMin) / ind);
        int ySteps = (int) Math.floor((yMax-yMin) / ind);
        xSteps += xSteps == 0 ? 1 : 0;
        ySteps += ySteps == 0 ? 1 : 0;
        double xStep = (xMax-xMin)/ xSteps;
        double yStep = (yMax-yMin)/ySteps;
        for (double x = xMin; x < xMax; x += xStep)
            for (double y =yMin; y <yMax; y += yStep)
                if (x+xStep/2 < xMax && y + yStep/2 < yMax)
                    nodes.add(new Node(x + xStep/2, y + yStep/2));
        // This is inefficient
        for (Node n : nodes) {
            for (Node n2 : nodes) {
                // Sufficient condition
                if (n != n2 && !n.getNeighbours().contains(n2) && Grid.areReachableNodes(n, n2)){
                    n.addNeighbour(n2);
                    n2.addNeighbour(n);
                }
            }
        }
    }

    public boolean neighbours(Quadrant other) {
        return (xMin == other.xMax || xMax == other.xMin) && (yMin >= other.yMin && yMin <= other.yMax || yMax >= other.yMin && yMax <= other.yMax) ||
                (yMin == other.yMax || yMax == other.yMin) && (xMin >= other.xMin && xMin <= other.xMax || xMax >= other.xMin && xMax <= other.xMax);
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Collection<Node> nodes) {
        this.nodes = nodes;
    }

}
