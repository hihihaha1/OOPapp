package src.gui;

import src.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class RobotModel {
    private double positionX = 100;
    private double positionY = 100;
    private double direction = 0;

    private final List<RobotPositionListener> listeners = new ArrayList<>();

    public interface RobotPositionListener {
        void onPositionChanged(double x, double y, double direction);
    }

    public void addListener(RobotPositionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RobotPositionListener listener) {
        listeners.remove(listener);
    }

    public void updatePosition(double x, double y, double direction) {
        this.positionX = x;
        this.positionY = y;
        this.direction = direction;
        notifyListeners();
        Logger.debug(String.format("Robot moved to: %.1f, %.1f, direction: %.2f", x, y, direction));
    }

    private void notifyListeners() {
        for (RobotPositionListener listener : listeners) {
            listener.onPositionChanged(positionX, positionY, direction);
        }
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }
}