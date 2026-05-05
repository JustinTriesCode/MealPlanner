package ui;

import ui.gui.MainFrame;
import ui.gui.panels.PeasOfCakeSplash;

// Main class to start app
public class Main {
    private static final boolean runGUI = true;
    // EFFECTS: Runs as GUI if runGUI is true, otherwise runs as cli
    
    public static void main(String[] args) {
        if (runGUI) {
            PeasOfCakeSplash logoScreen = new PeasOfCakeSplash();
            logoScreen.showSplash();
            new MainFrame();
        } else {
            new MealPlanApp();
        }
    }
}
