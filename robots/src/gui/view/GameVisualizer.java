package src.gui.view;

import src.gui.model.RobotModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer extends JPanel implements Observer {
    private final RobotModel robotModel;
    private final Timer repaintTimer = new Timer("redraw", true);

    private volatile double robotX;
    private volatile double robotY;
    private volatile double robotDirection;

    public GameVisualizer(RobotModel robotModel) {
        this.robotModel = robotModel;
        this.robotModel.addObserver(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robotModel.setTarget(e.getPoint());
            }
        });

        repaintTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(GameVisualizer.this::repaint);
            }
        }, 0, 50);

        setDoubleBuffered(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof double[]) {
            double[] positionData = (double[]) arg;
            this.robotX = positionData[0];
            this.robotY = positionData[1];
            this.robotDirection = positionData[2];
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawRobot((Graphics2D) g);
    }

    private void drawRobot(Graphics2D g) {
        int x = round(robotX);
        int y = round(robotY);

        AffineTransform transform = AffineTransform.getRotateInstance(robotDirection, x, y);
        g.setTransform(transform);

        g.setColor(Color.BLUE);
        fillOval(g, x, y, 30, 10);

        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);

        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);

        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
