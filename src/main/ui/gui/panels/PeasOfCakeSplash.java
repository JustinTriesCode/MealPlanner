package ui.gui.panels;

import javax.swing.*;
import java.awt.Color;

// A splash screen for the logo at start-up
public class PeasOfCakeSplash {
    private static final ImageIcon LOGO_ICON = new ImageIcon("src/main/resources/logo/PeasOfCakeLogo.png");
    private static final int DELAY_SECONDS = 2;
    private JWindow logoScreen;

    // EFFECTS: Set the logo and creates the screen
    public PeasOfCakeSplash() {
        logoScreen = new JWindow();
        logoScreen.setBackground(new Color(0, 0, 0, 0));
        JLabel logoLabel = new JLabel(LOGO_ICON);
        logoScreen.getContentPane().add(logoLabel);
        logoScreen.pack();
        logoScreen.setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: Shows splash screen for DELAY_SECONDS seconds
    public void showSplash() {
        logoScreen.setVisible(true);
        try {
            Thread.sleep(DELAY_SECONDS * 1000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        } finally {
            logoScreen.setVisible(false);
            logoScreen.dispose();
        }
    }
}
