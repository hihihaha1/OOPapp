package src.gui.model;

import src.log.Logger;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;

public class RobotModel extends Observable {
    private volatile double positionX = 100;
    private volatile double positionY = 100;
    private volatile double direction = 0;

    private volatile int targetX = 150;
    private volatile int targetY = 100;

    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.005;

    public RobotModel() {
        Timer timer = new Timer("robot-move-timer", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateModel();
            }
        }, 0, 10);
    }

    public void setTarget(Point p) {
        this.targetX = p.x;
        this.targetY = p.y;
    }

    private void updateModel() {
        double distance = distance(targetX, targetY, positionX, positionY);
        if (distance < 0.5) return;

        double velocity = MAX_VELOCITY;
        double angleToTarget = angleTo(positionX, positionY, targetX, targetY);
        double angularVelocity = 0;

        if (angleToTarget > direction) {
            angularVelocity = MAX_ANGULAR_VELOCITY;
        }
        if (angleToTarget < direction) {
            angularVelocity = -MAX_ANGULAR_VELOCITY;
        }

        moveRobot(velocity, angularVelocity, 10);

        double[] positionData = {positionX, positionY, direction};
        setChanged();
        notifyObservers(positionData);

        Logger.debug(String.format("Robot moved to: %.1f, %.1f, direction: %.2f", positionX, positionY, direction));
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, MAX_VELOCITY);
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);

        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = positionX + velocity * duration * Math.cos(direction);
        }
        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = positionY + velocity * duration * Math.sin(direction);
        }
        double newDirection = normalizeAngle(direction + angularVelocity * duration);

        positionX = newX;
        positionY = newY;
        direction = newDirection;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return normalizeAngle(Math.atan2(toY - fromY, toX - fromX));
    }

    private static double normalizeAngle(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    private static double applyLimits(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
