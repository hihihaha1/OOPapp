package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyVetoException;

public abstract class WindowCreator extends JInternalFrame {
    private static final String SETTINGS_FILE = "window_settings.txt";
    private static final String WINDOW_PREFIX = "window_";

    public WindowCreator(String title, boolean resizable, boolean closable,
                         boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        setIconifiable(true);
        initWindow();
        loadWindowState();
        setupWindowListeners();
    }

    private void initWindow() {
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        setLocation(50, 50);
    }

    private void setupWindowListeners() {
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

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                saveWindowState();
                dispose();
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                saveWindowState();
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                saveWindowState();
            }
        });
    }

    protected void saveWindowState() {
        Properties props = loadProperties();
        String prefix = WINDOW_PREFIX + getTitle() + "_";

        Rectangle bounds = getBounds();
        props.setProperty(prefix + "x", String.valueOf(bounds.x));
        props.setProperty(prefix + "y", String.valueOf(bounds.y));
        props.setProperty(prefix + "width", String.valueOf(bounds.width));
        props.setProperty(prefix + "height", String.valueOf(bounds.height));
        props.setProperty(prefix + "isIcon", String.valueOf(isIcon()));


        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            props.store(fos, "Window Settings");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении параметров окна " + getTitle() + ": " + e.getMessage());
        }
    }

    protected void loadWindowState() {
        Properties props = loadProperties();
        String prefix = WINDOW_PREFIX + getTitle() + "_";

        int x = Integer.parseInt(props.getProperty(prefix + "x", "50"));
        int y = Integer.parseInt(props.getProperty(prefix + "y", "50"));
        int width = Integer.parseInt(props.getProperty(prefix + "width", "400"));
        int height = Integer.parseInt(props.getProperty(prefix + "height", "300"));
        boolean isIcon = Boolean.parseBoolean(props.getProperty(prefix + "isIcon", "false"));

        setBounds(x, y, width, height);

        if (isIcon) {
            SwingUtilities.invokeLater(() -> {
                try {
                    setIcon(true);
                    setSelected(false);
                    System.out.println("Window " + getTitle() + " was minimized successfully.");
                } catch (PropertyVetoException e) {
                    System.err.println("Ошибка при сворачивании окна " + getTitle() + ": " + e.getMessage());
                }
            });
        }

        ensureVisible();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке настроек: " + e.getMessage());
            }
        }
        return props;
    }

    private void ensureVisible() {
        Rectangle bounds = getBounds();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (gc == null) return;

        Rectangle screenBounds = gc.getBounds();
        if (bounds.x < screenBounds.x) bounds.x = screenBounds.x;
        if (bounds.y < screenBounds.y) bounds.y = screenBounds.y;
        if (bounds.x + bounds.width > screenBounds.x + screenBounds.width) {
            bounds.x = screenBounds.x + screenBounds.width - bounds.width;
        }
        if (bounds.y + bounds.height > screenBounds.y + screenBounds.height) {
            bounds.y = screenBounds.y + screenBounds.height - bounds.height;
        }

        setBounds(bounds);
    }
}
