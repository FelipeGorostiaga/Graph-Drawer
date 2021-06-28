package ar.edu.itba;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import static ar.edu.itba.App.*;
import static ar.edu.itba.Graph.nodes;

public class Drawer extends JComponent {

    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color NODE_COLOR = Color.RED;
    private static final Color EDGE_COLOR = Color.BLUE;

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        // draw walls
        g2.setColor(WALL_COLOR);
        for (Wall wall : segments) {
            g2.draw(new Line2D.Double(wall.getP1().get(0) * scaleFactor, wall.getP1().get(1) * scaleFactor,
                    wall.getP2().get(0) * scaleFactor, wall.getP2().get(1) * scaleFactor));
        }
        // draw nodes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Node node : nodes) {
            g2.setColor(NODE_COLOR);
            Ellipse2D.Double dot = new Ellipse2D.Double(node.getX() * scaleFactor, node.getY() * scaleFactor, 2, 2);
            g2.draw(dot);

            // draw edges
            for (Node neighbour : node.getNeighbours()) {
                g2.setColor(EDGE_COLOR);
                g2.draw(new Line2D.Double(node.getX() * scaleFactor, node.getY() * scaleFactor,
                        neighbour.getX() * scaleFactor, neighbour.getY() * scaleFactor));
            }
        }
    }

    public static void init() {
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Drawer());
        frame.pack();
        int width = dimensions.get(0).intValue() * scaleFactor;
        int height = dimensions.get(1).intValue() * scaleFactor;
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
    }

}
