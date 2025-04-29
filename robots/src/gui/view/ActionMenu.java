package src.gui.view;

import src.gui.controller.MainApplicationFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class ActionMenu {
    private final MainApplicationFrame mainFrame;

    public ActionMenu(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    public JMenu createMenu() {
        JMenu actionMenu = new JMenu("Окно");
        actionMenu.setMnemonic(KeyEvent.VK_A);
        actionMenu.getAccessibleContext().setAccessibleDescription(
                "Действия приложения");

        JMenuItem exitItem = new JMenuItem("Выйти", KeyEvent.VK_X);
        exitItem.addActionListener((event) -> {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        });
        actionMenu.add(exitItem);

        return actionMenu;
    }
}