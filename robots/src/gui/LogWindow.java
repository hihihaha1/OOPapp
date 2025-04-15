package src.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LogWindow extends WindowCreator {
    private final JTextPane logTextPane;

    public LogWindow() {
        super("Протокол работы", true, true, true, true);

        logTextPane = new JTextPane();
        logTextPane.setContentType("text/html");
        logTextPane.setEditable(false);
        logTextPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        String htmlText = "Протокол работы";
        logTextPane.setText(htmlText);

        JScrollPane scrollPane = new JScrollPane(logTextPane);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        add(scrollPane);

        setPreferredSize(new Dimension(300, 100));
    }

    @Override
    protected void saveWindowState() {
        super.saveWindowState();
    }

    @Override
    protected void loadWindowState() {
        super.loadWindowState();
    }
}