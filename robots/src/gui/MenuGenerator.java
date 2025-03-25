package src.gui;

import javax.swing.*;

public class MenuGenerator {
    private final MainApplicationFrame mainFrame;

    public MenuGenerator(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    public JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        LookAndFeelMenu lookAndFeelMenu = new LookAndFeelMenu(mainFrame);
        menuBar.add(lookAndFeelMenu.createMenu());

        TestMenu testMenu = new TestMenu();
        menuBar.add(testMenu.createMenu());

        ActionMenu actionMenu = new ActionMenu(mainFrame);
        menuBar.add(actionMenu.createMenu());

        return menuBar;
    }
}