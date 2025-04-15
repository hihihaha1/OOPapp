package src.gui;

import javax.swing.*;

public class MenuGenerator {
    private final MainApplicationFrame frame;

    public MenuGenerator(MainApplicationFrame frame) {
        this.frame = frame;
    }

    public JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        return menuBar;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Вид");
        menu.add(createLookAndFeelItem("Системная", "javax.swing.plaf.metal.MetalLookAndFeel"));
        menu.add(createLookAndFeelItem("Универсальная", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
        menu.add(createLookAndFeelItem("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
        return menu;
    }

    private JMenuItem createLookAndFeelItem(String text, String lookAndFeel) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> frame.setLookAndFeel(lookAndFeel));
        return item;
    }
}