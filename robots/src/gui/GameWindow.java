package src.gui;

public class GameWindow extends WindowCreator {
    public GameWindow(RobotModel robotModel) {
        super("Игровое поле", true, true, true, true);
        add(new GameVisualizer(robotModel));
    }
}