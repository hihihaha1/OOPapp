package src.gui.view;

import src.gui.model.RobotModel;

public class GameWindow extends WindowCreator {
    public GameWindow(RobotModel robotModel) {
        super("Игровое поле", true, true, true, true);
        add(new GameVisualizer(robotModel));
    }
}