package ar.edu.itba;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import static ar.edu.itba.App.segments;
import static ar.edu.itba.Graph.nodes;

public class Drawer extends JComponent {

    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color NODE_COLOR = Color.RED;
    private static final Color EDGE_COLOR = Color.BLUE;

    @Override
    public void paint(Graphics g) {

        // Draw a simple line using the Graphics2D draw() method.
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(WALL_COLOR);
        for (Wall wall: segments) {
            g2.draw(new Line2D.Double(wall.getP1().get(0) * 10, wall.getP1().get(1) * 10,
                    wall.getP2().get(0) * 10 , wall.getP2().get(1) * 10));
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Node node: nodes) {

            g2.setColor(NODE_COLOR);
            Ellipse2D.Double dot = new Ellipse2D.Double(node.getX() * 10, node.getY() * 10, 1, 1);
            g2.draw(dot);

            for (Node neighbour: node.getNeighbours()) {

                g2.setColor(EDGE_COLOR);
                g2.draw(new Line2D.Double(node.getX() * 10,node.getY() * 10,
                        neighbour.getX() * 10, neighbour.getY() * 10));
            }
        }

    }

    public static void init() {
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Drawer());
        frame.pack();
        frame.setSize(new Dimension(420, 440));
        frame.setVisible(true);
    }

}
