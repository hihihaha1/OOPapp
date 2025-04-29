package src.gui.view;

import src.log.Logger;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.Properties;

public abstract class WindowCreator extends JInternalFrame {
    private static final String SETTINGS_FILE = "windows.properties";
    private static final String WINDOW_PREFIX = "window_";

    public WindowCreator(String title, boolean resizable, boolean closable,
                         boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setDefaultSizeAndPosition();
        setupListeners();
        loadState();
    }

    private void setDefaultSizeAndPosition() {
        setSize(400, 300);
        setLocation(50, 50);
    }

    private void setupListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override public void componentMoved(ComponentEvent e) { saveState(); }
            @Override public void componentResized(ComponentEvent e) { saveState(); }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override public void internalFrameClosing(InternalFrameEvent e) { saveState(); dispose(); }
            @Override public void internalFrameIconified(InternalFrameEvent e) { saveIconState(); }
            @Override public void internalFrameDeiconified(InternalFrameEvent e) { saveState(); }
        });
    }

    private synchronized void saveState() {
        Properties props = loadProperties();
        String prefix = WINDOW_PREFIX + getTitle() + "_";
        props.setProperty(prefix + "x", String.valueOf(getX()));
        props.setProperty(prefix + "y", String.valueOf(getY()));
        props.setProperty(prefix + "width", String.valueOf(getWidth()));
        props.setProperty(prefix + "height", String.valueOf(getHeight()));
        saveProperties(props);
    }

    private void saveIconState() {
        try {
            setIcon(true);
            saveState();
        } catch (PropertyVetoException e) {
            Logger.error("Не удалось свернуть окно: " + getTitle());
        }
    }

    private synchronized void loadState() {
        Properties props = loadProperties();
        String prefix = WINDOW_PREFIX + getTitle() + "_";

        setBounds(
                Integer.parseInt(props.getProperty(prefix + "x", String.valueOf(getX()))),
                Integer.parseInt(props.getProperty(prefix + "y", String.valueOf(getY()))),
                Integer.parseInt(props.getProperty(prefix + "width", String.valueOf(getWidth()))),
                Integer.parseInt(props.getProperty(prefix + "height", String.valueOf(getHeight())))
        );

        if (Boolean.parseBoolean(props.getProperty(prefix + "isIcon", "false"))) {
            try { setIcon(true); }
            catch (PropertyVetoException e) { Logger.error("Ошибка восстановления состояния окна"); }
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(SETTINGS_FILE)) {
            props.load(is);
        } catch (IOException ignored) {}
        return props;
    }

    private void saveProperties(Properties props) {
        try (OutputStream os = new FileOutputStream(SETTINGS_FILE)) {
            props.store(os, "Window states");
        } catch (IOException e) {
            Logger.error("Ошибка сохранения состояния окон");
        }
    }

    protected Properties getProperties() {
        Properties props = new Properties();
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                props.load(is);
            } catch (IOException ignored) {}
        }
        return props;
    }

    protected void saveAdditionalProperties(Properties additionalProps) {
        Properties props = getProperties();
        String prefix = WINDOW_PREFIX + getTitle() + "_";

        additionalProps.forEach((key, value) ->
                props.setProperty(prefix + key, value.toString()));

        saveProperties(props);
    }

    protected void saveWindowState() {
    }

    protected void loadWindowState() {

    }
}