package src.gui;

import src.log.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final Preferences prefs = Preferences.userRoot().node("robots_app");

    public MainApplicationFrame() {
        initMainFrame();
        setupWindows();
        setupOriginalMenuBar();
        setupListeners();
    }

    private void initMainFrame() {
        setBounds(
                prefs.getInt("main_x", 100),
                prefs.getInt("main_y", 100),
                prefs.getInt("main_width", 800),
                prefs.getInt("main_height", 600)
        );
        setContentPane(desktopPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void setupOriginalMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu testsMenu = new JMenu("Тесты");
        JMenuItem testItem = new JMenuItem("Выполнить тест");
        testItem.addActionListener(e -> Logger.debug("Тест выполнен"));
        testsMenu.add(testItem);
        menuBar.add(testsMenu);

        JMenu displayMenu = new JMenu("Режим отображения");
        JMenuItem systemLook = new JMenuItem("Системный стиль");
        systemLook.addActionListener(e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));

        JMenuItem crossPlatformLook = new JMenuItem("Универсальный стиль");
        crossPlatformLook.addActionListener(e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));

        displayMenu.add(systemLook);
        displayMenu.add(crossPlatformLook);
        menuBar.add(displayMenu);

        JMenu actionsMenu = new JMenu("Действия");
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> confirmExit());
        actionsMenu.add(exitItem);
        menuBar.add(actionsMenu);

        setJMenuBar(menuBar);
    }

    private void setupWindows() {
        RobotModel robotModel = new RobotModel();
        addWindow(new LogWindow());
        addWindow(new GameWindow(robotModel));
        addWindow(new RobotCoordinatesWindow(robotModel));
    }

    private void setupListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                saveWindowState();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                saveWindowState();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                saveWindowState();
            }
        });
    }

    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);

            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                SwingUtilities.updateComponentTreeUI(frame);
            }

            Logger.debug("Установлен стиль: " + className);
        } catch (Exception e) {
            Logger.error("Ошибка установки стиля: " + className);
        }
    }

    private void saveWindowState() {
        prefs.putInt("main_x", getX());
        prefs.putInt("main_y", getY());
        prefs.putInt("main_width", getWidth());
        prefs.putInt("main_height", getHeight());
    }

    public void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите выйти?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}