package ui.gui.panels;

import javax.swing.*;

import ui.gui.MainFrame;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Abstract class representation of panel to ensure consistent styling and
// access
public abstract class BasePanel extends JPanel implements ActionListener {
    protected static final Color BACKGROUND_COLOUR = new Color(45, 52, 54);
    protected static final Color BUTTON_COLOUR = new Color(245, 247, 245);
    protected static final Font MAIN_FONT = new Font("Segoe Print", Font.BOLD, 18);
    protected MainFrame controller;

    // EFFECTS: Sets background color and controller
    public BasePanel(MainFrame controller) {
        this.controller = controller;
        setBackground(BACKGROUND_COLOUR);
    }

    // MODIFIES: component
    // EFFECTS: Helper that applies format to components
    public void fontSetter(Component component) {
        component.setFont(MAIN_FONT);
    }

    // EFFECTS: Sets button with just text and command
    public JButton makeButton(String text, String command) {
        return makeButton(text, command, null);
    }

    // EFFECTS: Creates button with text and command, and icon if not null
    public JButton makeButton(String text, String command, Icon icon) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.addActionListener(this);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.setBackground(BUTTON_COLOUR);
        btn.setMnemonic(text.charAt(0));
        add(btn);
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: Passes the panel selected to MainFrame to update the panel displayed
    public void actionPerformed(ActionEvent e) {
        controller.showView(e.getActionCommand());
    }

    // EFFECTS: Returns the MainFrame
    public MainFrame getController() {
        return controller;
    }

}
