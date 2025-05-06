package src.gui.model;

import src.gui.view.WindowCreator;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RobotCoordinatesWindow extends WindowCreator implements Observer {
    private final JLabel coordinatesLabel;

    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);

        coordinatesLabel = new JLabel("Ожидание данных...", SwingConstants.CENTER);
        coordinatesLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinatesLabel, BorderLayout.CENTER);
        add(panel);

        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof double[]) {
            double[] positionData = (double[]) arg;
            SwingUtilities.invokeLater(() -> {
                String text = String.format(
                        "X: %.1f  Y: %.1f  Направление: %.2f",
                        positionData[0],
                        positionData[1],
                        positionData[2]
                );
                coordinatesLabel.setText(text);
            });
        }
    }
}
