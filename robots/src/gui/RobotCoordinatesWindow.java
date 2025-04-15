package src.gui;

import javax.swing.*;
import java.awt.*;

public class RobotCoordinatesWindow extends WindowCreator implements RobotModel.RobotPositionListener {
    private final JLabel coordinatesLabel;

    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        coordinatesLabel = new JLabel("Ожидание данных...", SwingConstants.CENTER);
        coordinatesLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinatesLabel, BorderLayout.CENTER);
        add(panel);

        model.addListener(this);
    }

    @Override
    public void onPositionChanged(double x, double y, double direction) {
        SwingUtilities.invokeLater(() -> {
            String text = String.format("X: %.1f  Y: %.1f  Направление: %.2f", x, y, direction);
            coordinatesLabel.setText(text);
        });
    }
}